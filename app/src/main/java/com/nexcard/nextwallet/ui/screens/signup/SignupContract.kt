package com.nexcard.nextwallet.ui.screens.signup

data class SignupUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null,
    val errorName: String? = null,
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorConfirm: String? = null,
)

sealed interface SignupEvent {
    data class NameChanged(val value: String) : SignupEvent
    data class EmailChanged(val value: String) : SignupEvent
    data class PasswordChanged(val value: String) : SignupEvent
    data class ConfirmChanged(val value: String) : SignupEvent
    data object TogglePassword : SignupEvent
    data object Submit : SignupEvent
}
