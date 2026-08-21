package com.nexcard.nextwallet.ui.screens.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val remember: Boolean = true,
    val isLoading: Boolean = false,
    val showPassword: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface LoginUiEvent {
    data class EmailChanged(val value: String) : LoginUiEvent
    data class PasswordChanged(val value: String) : LoginUiEvent
    data class RememberChanged(val value: Boolean) : LoginUiEvent
    data object TogglePasswordVisibility : LoginUiEvent
    data object Submit : LoginUiEvent
}

sealed interface LoginEffect {
    data object GoHome : LoginEffect
    data object GoSignup : LoginEffect
}
