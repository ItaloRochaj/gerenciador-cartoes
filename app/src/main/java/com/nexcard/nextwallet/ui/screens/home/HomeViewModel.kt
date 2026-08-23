package com.nexcard.nextwallet.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexcard.nextwallet.data.local.datastore.PreferencesDataStore
import com.nexcard.nextwallet.domain.repository.WalletRepository
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
class HomeViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val preferencesDataStore: PreferencesDataStore,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState(loadState = ScreenLoadState.Success))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                walletRepository.refreshCards()
                walletRepository.refreshTransactions()
            }
            try {
                combine(
                    walletRepository.observeCards(),
                    walletRepository.observeTransactions(),
                    preferencesDataStore.userName,
                    preferencesDataStore.lastCardId,
                ) { cards, transactions, name, lastCardId ->
                    val primaryCard = cards.firstOrNull { it.id == lastCardId } ?: cards.firstOrNull()
                    val recentTransactions = transactions
                        .asSequence()
                        .filter { tx -> primaryCard == null || tx.cardId == primaryCard.id }
                        .sortedByDescending { it.dateEpochMillis }
                        .take(6)
                        .toList()

                    HomeUiState(
                        loadState = ScreenLoadState.Success,
                        userName = name.ifBlank { "Usuário" },
                        primaryCard = primaryCard,
                        recentTransactions = recentTransactions,
                    )
                }.collect { _state.value = it }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        loadState = ScreenLoadState.Error(e.message ?: "Erro ao carregar dados"),
                    )
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                walletRepository.refreshCards()
                walletRepository.refreshTransactions()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        loadState = ScreenLoadState.Error(e.message ?: "Erro ao atualizar dados"),
                    )
                }
            }
        }
    }
}
