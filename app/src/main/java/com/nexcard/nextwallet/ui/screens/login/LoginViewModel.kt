package com.nexcard.nextwallet.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexcard.nextwallet.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> _state.update { it.copy(email = event.value, errorMessage = null) }
            is LoginUiEvent.PasswordChanged -> _state.update { it.copy(password = event.value, errorMessage = null) }
            is LoginUiEvent.RememberChanged -> _state.update { it.copy(remember = event.value) }
            LoginUiEvent.TogglePasswordVisibility -> _state.update { it.copy(showPassword = !it.showPassword) }
            LoginUiEvent.Submit -> submit()
        }
    }

    fun goSignup() {
        viewModelScope.launch { _effect.send(LoginEffect.GoSignup) }
    }

    private fun submit() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val current = _state.value
            val result = loginUseCase(current.email.trim(), current.password, current.remember)
            result.onSuccess {
                _effect.send(LoginEffect.GoHome)
            }.onFailure {
                _state.update { s -> s.copy(errorMessage = it.message ?: "Falha ao autenticar.") }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}
