package com.nexcard.nextwallet.ui.screens.addcard

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
class AddCardViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AddCardUiState(loadState = ScreenLoadState.Loading))
    val state: StateFlow<AddCardUiState> = _state.asStateFlow()

    init {
        refresh()
        viewModelScope.launch {
            walletRepository.observeProducts().collect { products ->
                _state.update {
                    it.copy(
                        products = products,
                        loadState = if (products.isEmpty()) ScreenLoadState.Empty else ScreenLoadState.Success,
                        selectedProductId = if (it.selectedProductId.isBlank()) products.firstOrNull()?.id.orEmpty() else it.selectedProductId,
                    )
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(loadState = ScreenLoadState.Loading) }
            walletRepository.refreshProducts().onFailure {
                _state.update { s -> s.copy(loadState = ScreenLoadState.Error("Não foi possível carregar os produtos de cartão.")) }
            }
        }
    }

    fun selectProduct(id: String) = _state.update { it.copy(selectedProductId = id) }
    fun selectStyle(style: String) = _state.update { it.copy(selectedStyle = style) }
    fun clearMessage() = _state.update { it.copy(message = null) }

    fun requestCard(onSuccess: () -> Unit) {
        val current = _state.value
        if (current.selectedProductId.isBlank() || current.isRequesting) return
        viewModelScope.launch {
            _state.update { it.copy(isRequesting = true) }
            val result = walletRepository.requestCard(current.selectedProductId, current.selectedStyle)
            _state.update {
                it.copy(
                    isRequesting = false,
                    message = result.exceptionOrNull()?.message ?: "Solicitação realizada com sucesso!",
                )
            }
            if (result.isSuccess) onSuccess()
        }
    }
}
