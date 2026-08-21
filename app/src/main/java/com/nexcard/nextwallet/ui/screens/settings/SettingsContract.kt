package com.nexcard.nextwallet.ui.screens.settings

import com.nexcard.nextwallet.domain.model.ThemeMode

data class SettingsUiState(
    val userName: String = "Usuário Next Wallet",
    val email: String = "usuario@nextwallet.com",
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val notifications: Boolean = true,
)
