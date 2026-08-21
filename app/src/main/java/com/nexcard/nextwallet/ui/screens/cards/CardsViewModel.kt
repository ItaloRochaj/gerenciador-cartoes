package com.nexcard.nextwallet.ui.screens.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexcard.nextwallet.domain.repository.WalletRepository
import com.nexcard.nextwallet.util.ScreenLoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CardsUiState(loadState = ScreenLoadState.Loading))
    val state: StateFlow<CardsUiState> = _state.asStateFlow()

    init {
        refresh()
        viewModelScope.launch {
            walletRepository.observeCards().collect { cards ->
                _state.update {
                    it.copy(
                        cards = cards.sortedByDescending { c -> c.isFavorite },
                        loadState = if (cards.isEmpty()) ScreenLoadState.Empty else ScreenLoadState.Success,
                    )
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(loadState = ScreenLoadState.Loading) }
            walletRepository.refreshCards()
                .onFailure {
                    _state.update { s -> s.copy(loadState = ScreenLoadState.Error("Não foi possível carregar os cartões.")) }
                }
        }
    }

    fun toggleFavorite(cardId: String, favorite: Boolean) {
        viewModelScope.launch { walletRepository.setFavorite(cardId, favorite) }
    }

    fun askDelete(cardId: String) {
        _state.update { it.copy(deletingCardId = cardId) }
    }

    fun clearDeleteRequest() {
        _state.update { it.copy(deletingCardId = null) }
    }

    fun deleteConfirmed() {
        val id = _state.value.deletingCardId ?: return
        viewModelScope.launch {
            walletRepository.deleteCard(id)
            _state.update { it.copy(deletingCardId = null) }
        }
    }
}
