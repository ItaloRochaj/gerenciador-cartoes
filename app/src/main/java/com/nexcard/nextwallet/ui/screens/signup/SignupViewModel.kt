package com.nexcard.nextwallet.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexcard.nextwallet.domain.usecase.LoginUseCase
import com.nexcard.nextwallet.domain.usecase.SignupUseCase
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
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SignupUiState())
    val state: StateFlow<SignupUiState> = _state.asStateFlow()

    private val _goLogin = Channel<Unit>(Channel.BUFFERED)
    val goLogin = _goLogin.receiveAsFlow()

    fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.NameChanged -> _state.update { it.copy(name = event.value, errorName = null, message = null) }
            is SignupEvent.EmailChanged -> _state.update { it.copy(email = event.value, errorEmail = null, message = null) }
            is SignupEvent.PasswordChanged -> _state.update { it.copy(password = event.value, errorPassword = null, message = null) }
            is SignupEvent.ConfirmChanged -> _state.update { it.copy(confirmPassword = event.value, errorConfirm = null, message = null) }
            SignupEvent.TogglePassword -> _state.update { it.copy(showPassword = !it.showPassword) }
            SignupEvent.Submit -> submit()
        }
    }

    fun onGoLoginClick() {
        viewModelScope.launch { _goLogin.send(Unit) }
    }

    private fun submit() {
        val current = _state.value
        val invalid = validate(current)
        if (invalid) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = signupUseCase(current.name.trim(), current.email.trim(), current.password)
            result.onFailure {
                _state.update { s -> s.copy(message = it.message ?: "Falha no cadastro.", isLoading = false) }
                return@launch
            }
            loginUseCase(current.email.trim(), current.password, true)
            _state.update { it.copy(isLoading = false, message = "Cadastro realizado com sucesso!") }
        }
    }

    private fun validate(state: SignupUiState): Boolean {
        var hasError = false
        if (state.name.isBlank()) {
            _state.update { it.copy(errorName = "Nome obrigatório.") }
            hasError = true
        }
        if (!Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(state.email.trim())) {
            _state.update { it.copy(errorEmail = "Formato de e-mail inválido.") }
            hasError = true
        }
        if (state.password.length < 6) {
            _state.update { it.copy(errorPassword = "Senha deve ter ao menos 6 caracteres.") }
            hasError = true
        }
        if (state.confirmPassword != state.password) {
            _state.update { it.copy(errorConfirm = "A confirmação deve ser igual à senha.") }
            hasError = true
        }
        return hasError
    }
}
