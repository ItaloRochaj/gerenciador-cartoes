package com.nexcard.nextwallet.ui.screens.financial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.domain.repository.WalletRepository
import com.nexcard.nextwallet.domain.usecase.ChangeCardLimitUseCase
import com.nexcard.nextwallet.domain.usecase.RegisterPurchaseUseCase
import com.nexcard.nextwallet.util.ScreenLoadState
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
    private val registerPurchaseUseCase: RegisterPurchaseUseCase,
    private val changeCardLimitUseCase: ChangeCardLimitUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(FinancialUiState(loadState = ScreenLoadState.Loading))
    val state: StateFlow<FinancialUiState> = _state.asStateFlow()

    init {
        refresh()
        viewModelScope.launch {
            combine(
                walletRepository.observeCards(),
                walletRepository.observeTransactions(),
            ) { cards, transactions ->
                val selected = _state.value.selectedCardId.ifBlank { cards.firstOrNull()?.id.orEmpty() }
                val filtered = transactions.filter { it.cardId == selected }
                _state.value.copy(
                    cards = cards,
                    selectedCardId = selected,
                    transactions = filtered,
                    loadState = if (cards.isEmpty()) ScreenLoadState.Empty else ScreenLoadState.Success,
                )
            }.collect { _state.value = it }
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

    fun selectCard(cardId: String) = _state.update { it.copy(selectedCardId = cardId) }
    fun togglePurchaseSheet(show: Boolean) = _state.update { it.copy(showPurchaseSheet = show) }
    fun toggleLimitSheet(show: Boolean) = _state.update { it.copy(showLimitSheet = show) }
    fun toggleSort() = _state.update { it.copy(sortByValue = !it.sortByValue) }
    fun setCategory(category: TransactionCategory?) = _state.update { it.copy(selectedCategory = category) }
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
}
