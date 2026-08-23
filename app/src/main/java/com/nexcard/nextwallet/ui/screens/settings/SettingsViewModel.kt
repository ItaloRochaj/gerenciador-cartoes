package com.nexcard.nextwallet.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexcard.nextwallet.data.local.datastore.PreferencesDataStore
import com.nexcard.nextwallet.domain.model.ThemeMode
import com.nexcard.nextwallet.domain.repository.SettingsRepository
import com.nexcard.nextwallet.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val preferencesDataStore: PreferencesDataStore,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                preferencesDataStore.userName,
                preferencesDataStore.userEmail,
                settingsRepository.themeMode,
                settingsRepository.notificationsEnabled,
            ) { name, email, theme, notifications ->
                SettingsUiState(
                    userName = name.ifBlank { "Italo Rocha" },
                    email = email.ifBlank { "usuario@nextwallet.com" },
                    themeMode = theme,
                    notifications = notifications,
                )
            }.collect { _state.value = it }
        }
    }

    fun setTheme(mode: ThemeMode) {
        viewModelScope.launch { settingsRepository.setThemeMode(mode) }
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setNotificationsEnabled(enabled) }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onDone()
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            preferencesDataStore.resetApplicationData()
        }
    }
}
