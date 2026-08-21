package com.nexcard.nextwallet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexcard.nextwallet.domain.model.ThemeMode
import com.nexcard.nextwallet.domain.repository.AuthRepository
import com.nexcard.nextwallet.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AppViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    val sessionActive: StateFlow<Boolean> = authRepository.isSessionActive()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val themeMode: StateFlow<ThemeMode> = settingsRepository.themeMode
        .stateIn(viewModelScope, SharingStarted.Lazily, ThemeMode.SYSTEM)

    val notificationsEnabled: StateFlow<Boolean> = settingsRepository.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    fun setTheme(mode: ThemeMode) {
        viewModelScope.launch { 
            try {
                settingsRepository.setThemeMode(mode)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch { 
            try {
                settingsRepository.setNotificationsEnabled(enabled)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
