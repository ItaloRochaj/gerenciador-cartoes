package com.nexcard.nextwallet.ui.screens.financial

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.domain.repository.SettingsRepository
import com.nexcard.nextwallet.domain.repository.WalletRepository
import com.nexcard.nextwallet.domain.usecase.ChangeCardLimitUseCase
import com.nexcard.nextwallet.domain.usecase.RegisterPurchaseUseCase
import com.nexcard.nextwallet.ui.navigation.AppRoute
import com.nexcard.nextwallet.util.ScreenLoadState
import java.util.Calendar
import java.util.Locale
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FinancialViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val settingsRepository: SettingsRepository,
    private val registerPurchaseUseCase: RegisterPurchaseUseCase,
    private val changeCardLimitUseCase: ChangeCardLimitUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(FinancialUiState(loadState = ScreenLoadState.Loading))
    val state: StateFlow<FinancialUiState> = _state.asStateFlow()

    private val selectedCardIdFlow = MutableStateFlow(
        savedStateHandle[AppRoute.Financial.ARG_CARD_ID]
            ?: savedStateHandle[AppRoute.Consolidated.ARG_CARD_ID]
            ?: "",
    )
    private val selectedMonthFlow = MutableStateFlow(
        savedStateHandle[AppRoute.Financial.ARG_MONTH]
            ?: savedStateHandle[AppRoute.Consolidated.ARG_MONTH]
            ?: "",
    )
    private val selectedCategoryFlow = MutableStateFlow<TransactionCategory?>(null)
    private val sortByValueFlow = MutableStateFlow(false)

    init {
        refresh()
        viewModelScope.launch {
            val baseDataFlow = combine(
                walletRepository.observeCards(),
                walletRepository.observeTransactions(),
                walletRepository.observeInvoices(),
                settingsRepository.lastCardId,
            ) { cards, transactions, invoices, lastCardId ->
                BaseFinancialData(cards, transactions, invoices, lastCardId)
            }

            combine(
                baseDataFlow,
                selectedCardIdFlow,
                selectedMonthFlow,
                selectedCategoryFlow,
                sortByValueFlow,
            ) { base, selectedCardId, selectedMonth, selectedCategory, sortByValue ->
                val resolvedCardId = resolveSelectedCardId(base.cards.map { it.id }, selectedCardId, base.lastCardId)
                val resolvedMonth = selectedMonth.ifBlank { currentYearMonthKey() }
                val filteredInvoices = base.invoices.filter { it.cardId == resolvedCardId }

                val monthlyTransactions = base.transactions
                    .asSequence()
                    .filter { it.cardId == resolvedCardId }
                    .filter { referenceMonthFromEpoch(it.dateEpochMillis) == resolvedMonth }
                    .filter { tx -> selectedCategory == null || tx.category == selectedCategory }
                    .let { source ->
                        if (sortByValue) source.sortedByDescending { it.amountCents }
                        else source.sortedByDescending { it.dateEpochMillis }
                    }
                    .toList()

                FinancialProjection(
                    cards = base.cards,
                    invoices = filteredInvoices,
                    selectedCardId = resolvedCardId,
                    selectedReferenceMonth = resolvedMonth,
                    transactions = monthlyTransactions,
                    selectedCategory = selectedCategory,
                    sortByValue = sortByValue,
                    loadState = if (base.cards.isEmpty()) ScreenLoadState.Empty else ScreenLoadState.Success,
                )
            }.collect { projection ->
                _state.update {
                    it.copy(
                        cards = projection.cards,
                        invoices = projection.invoices,
                        selectedCardId = projection.selectedCardId,
                        selectedReferenceMonth = projection.selectedReferenceMonth,
                        transactions = projection.transactions,
                        selectedCategory = projection.selectedCategory,
                        sortByValue = projection.sortByValue,
                        loadState = projection.loadState,
                    )
                }

                if (selectedCardIdFlow.value != projection.selectedCardId) {
                    selectedCardIdFlow.value = projection.selectedCardId
                }
                if (selectedMonthFlow.value != projection.selectedReferenceMonth) {
                    selectedMonthFlow.value = projection.selectedReferenceMonth
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(loadState = ScreenLoadState.Loading) }
            val c = walletRepository.refreshCards()
            walletRepository.refreshTransactions()
            walletRepository.refreshProducts()
            if (c.isFailure) {
                _state.update { it.copy(loadState = ScreenLoadState.Error("Verifique a conexão e tente novamente.")) }
            }
        }
    }

    fun selectCard(cardId: String) {
        selectedCardIdFlow.value = cardId
        selectedMonthFlow.value = currentYearMonthKey()
        viewModelScope.launch { settingsRepository.setLastCard(cardId) }
    }

    fun selectNextCard() {
        val cards = _state.value.cards
        if (cards.isEmpty()) return
        val currentIndex = cards.indexOfFirst { it.id == _state.value.selectedCardId }
        val nextIndex = if (currentIndex == -1) 0 else (currentIndex + 1) % cards.size
        selectCard(cards[nextIndex].id)
    }

    fun selectInvoiceMonth(referenceMonth: String) {
        selectedMonthFlow.value = referenceMonth
    }

    fun togglePurchaseSheet(show: Boolean) = _state.update { it.copy(showPurchaseSheet = show) }
    fun toggleLimitSheet(show: Boolean) = _state.update { it.copy(showLimitSheet = show) }
    fun toggleSort() {
        sortByValueFlow.value = !sortByValueFlow.value
    }

    fun setCategory(category: TransactionCategory?) {
        selectedCategoryFlow.value = category
    }

    fun clearMessage() = _state.update { it.copy(message = null) }

    fun registerPurchase(description: String, amountCents: Long, category: TransactionCategory) {
        val cardId = _state.value.selectedCardId
        if (cardId.isBlank()) return
        viewModelScope.launch {
            val result = registerPurchaseUseCase(cardId, description, amountCents, category, System.currentTimeMillis())
            _state.update {
                it.copy(
                    message = result.exceptionOrNull()?.message ?: "Compra registrada com sucesso.",
                    showPurchaseSheet = result.isFailure,
                )
            }
            if (result.isSuccess) togglePurchaseSheet(false)
        }
    }

    fun changeLimit(newLimitCents: Long) {
        val cardId = _state.value.selectedCardId
        if (cardId.isBlank()) return
        viewModelScope.launch {
            val result = changeCardLimitUseCase(cardId, newLimitCents)
            _state.update {
                it.copy(
                    message = result.exceptionOrNull()?.message ?: "Limite atualizado com sucesso.",
                    showLimitSheet = result.isFailure,
                )
            }
            if (result.isSuccess) toggleLimitSheet(false)
        }
    }

    private fun resolveSelectedCardId(
        cardIds: List<String>,
        preferredCardId: String,
        lastCardId: String,
    ): String {
        if (preferredCardId.isNotBlank() && cardIds.contains(preferredCardId)) return preferredCardId
        if (lastCardId.isNotBlank() && cardIds.contains(lastCardId)) return lastCardId
        return cardIds.firstOrNull().orEmpty()
    }

    private fun referenceMonthFromEpoch(epochMillis: Long): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = epochMillis }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        return String.format(Locale.US, "%04d-%02d", year, month)
    }

    private fun currentYearMonthKey(): String = referenceMonthFromEpoch(System.currentTimeMillis())

    private data class FinancialProjection(
        val cards: List<com.nexcard.nextwallet.domain.model.Card>,
        val invoices: List<com.nexcard.nextwallet.domain.model.Invoice>,
        val selectedCardId: String,
        val selectedReferenceMonth: String,
        val transactions: List<com.nexcard.nextwallet.domain.model.Transaction>,
        val selectedCategory: TransactionCategory?,
        val sortByValue: Boolean,
        val loadState: ScreenLoadState,
    )

    private data class BaseFinancialData(
        val cards: List<com.nexcard.nextwallet.domain.model.Card>,
        val transactions: List<com.nexcard.nextwallet.domain.model.Transaction>,
        val invoices: List<com.nexcard.nextwallet.domain.model.Invoice>,
        val lastCardId: String,
    )
}
