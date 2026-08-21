package com.nexcard.nextwallet.data.repository

import com.nexcard.nextwallet.data.local.datastore.PreferencesDataStore
import com.nexcard.nextwallet.domain.model.ThemeMode
import com.nexcard.nextwallet.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore,
) : SettingsRepository {
    override val themeMode: Flow<ThemeMode> = preferencesDataStore.themeMode
    override val notificationsEnabled: Flow<Boolean> = preferencesDataStore.notificationsEnabled

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        preferencesDataStore.setThemeMode(themeMode)
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        preferencesDataStore.setNotifications(enabled)
    }

    override suspend fun setLastCard(cardId: String) {
        preferencesDataStore.setLastCardId(cardId)
    }
}
