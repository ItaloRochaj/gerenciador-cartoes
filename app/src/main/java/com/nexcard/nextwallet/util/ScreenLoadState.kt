package com.nexcard.nextwallet.util

sealed interface ScreenLoadState {
    data object Idle : ScreenLoadState
    data object Loading : ScreenLoadState
    data object Empty : ScreenLoadState
    data class Error(val message: String) : ScreenLoadState
    data object Success : ScreenLoadState
}
