package com.nexcard.nextwallet.ui.screens.carddetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexcard.nextwallet.domain.repository.SettingsRepository
import com.nexcard.nextwallet.domain.repository.WalletRepository
import com.nexcard.nextwallet.domain.usecase.ToggleCardBlockUseCase
import com.nexcard.nextwallet.util.ScreenLoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val walletRepository: WalletRepository,
    private val settingsRepository: SettingsRepository,
    private val toggleCardBlockUseCase: ToggleCardBlockUseCase,
) : ViewModel() {
    private val cardId: String = savedStateHandle["cardId"] ?: ""
    private val _state = MutableStateFlow(CardDetailUiState())
    val state: StateFlow<CardDetailUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            walletRepository.observeCards().collect { cards ->
                val card = cards.firstOrNull { it.id == cardId }
                _state.update {
                    it.copy(
                        card = card,
                        loadState = if (card == null) ScreenLoadState.Error("Cartão não encontrado.") else ScreenLoadState.Success,
                    )
                }
            }
        }
        viewModelScope.launch { settingsRepository.setLastCard(cardId) }
    }

    fun toggleFavorite() {
        val card = _state.value.card ?: return
        viewModelScope.launch { walletRepository.setFavorite(card.id, !card.isFavorite) }
    }

    fun askBlockToggle() = _state.update { it.copy(showBlockConfirm = true) }
    fun dismissBlockToggle() = _state.update { it.copy(showBlockConfirm = false) }
    fun askDelete() = _state.update { it.copy(showDeleteConfirm = true) }
    fun dismissDelete() = _state.update { it.copy(showDeleteConfirm = false) }
    fun toggleVirtualCard() = _state.update { it.copy(showVirtualCard = !it.showVirtualCard) }
    fun flipVirtualCard() = _state.update { it.copy(showCardBack = !it.showCardBack) }
    fun toggleNumber() = _state.update { it.copy(revealNumber = !it.revealNumber) }
    fun clearMessage() = _state.update { it.copy(message = null) }

    fun toggleCvv() {
        _state.update { it.copy(showCardBack = true, revealCvv = true) }
        viewModelScope.launch {
            delay(4000)
            _state.update { it.copy(revealCvv = false) }
        }
    }

    fun confirmToggleBlock() {
        val card = _state.value.card ?: return
        viewModelScope.launch {
            val result = toggleCardBlockUseCase(card.id)
            _state.update {
                it.copy(
                    showBlockConfirm = false,
                    message = result.exceptionOrNull()?.message ?: "Status do cartão atualizado.",
                )
            }
        }
    }

    fun confirmDelete(onDeleted: () -> Unit) {
        val card = _state.value.card ?: return
        viewModelScope.launch {
            val result = walletRepository.deleteCard(card.id)
            _state.update {
                it.copy(
                    showDeleteConfirm = false,
                    message = result.exceptionOrNull()?.message ?: "Cartão removido com sucesso.",
                )
            }
            if (result.isSuccess) onDeleted()
        }
    }
}
