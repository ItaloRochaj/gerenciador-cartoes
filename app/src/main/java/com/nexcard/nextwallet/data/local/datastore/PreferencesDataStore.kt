package com.nexcard.nextwallet.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nexcard.nextwallet.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "next_wallet_prefs")

@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val sessionActive = booleanPreferencesKey("session_active")
        val rememberedEmail = stringPreferencesKey("remembered_email")
        val notifications = booleanPreferencesKey("notifications")
        val biometrics = booleanPreferencesKey("biometrics")
        val themeMode = stringPreferencesKey("theme_mode")
        val userName = stringPreferencesKey("user_name")
        val userEmail = stringPreferencesKey("user_email")
        val lastCardId = stringPreferencesKey("last_card_id")
    }

    val sessionActive: Flow<Boolean> = context.dataStore.data.map { it[Keys.sessionActive] ?: false }
    val rememberedEmail: Flow<String> = context.dataStore.data.map { it[Keys.rememberedEmail] ?: "" }
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.notifications] ?: true }
    val themeMode: Flow<ThemeMode> = context.dataStore.data.map {
        ThemeMode.valueOf(it[Keys.themeMode] ?: ThemeMode.SYSTEM.name)
    }
    val userName: Flow<String> = context.dataStore.data.map { it[Keys.userName] ?: "" }
    val userEmail: Flow<String> = context.dataStore.data.map { it[Keys.userEmail] ?: "" }
    val lastCardId: Flow<String> = context.dataStore.data.map { it[Keys.lastCardId] ?: "" }

    suspend fun setSession(active: Boolean) = context.dataStore.edit { it[Keys.sessionActive] = active }

    suspend fun saveUserSession(name: String, email: String, remember: Boolean) {
        context.dataStore.edit {
            it[Keys.sessionActive] = true
            it[Keys.userName] = name
            it[Keys.userEmail] = email
            if (remember) it[Keys.rememberedEmail] = email else it.remove(Keys.rememberedEmail)
        }
    }

    suspend fun clearSession() = context.dataStore.edit {
        it[Keys.sessionActive] = false
        it[Keys.userName] = ""
        it[Keys.userEmail] = ""
    }

    suspend fun setThemeMode(mode: ThemeMode) = context.dataStore.edit { it[Keys.themeMode] = mode.name }

    suspend fun setNotifications(enabled: Boolean) = context.dataStore.edit { it[Keys.notifications] = enabled }

    suspend fun setBiometrics(enabled: Boolean) = context.dataStore.edit { it[Keys.biometrics] = enabled }

    suspend fun setLastCardId(cardId: String) = context.dataStore.edit { it[Keys.lastCardId] = cardId }

    suspend fun resetApplicationData() = context.dataStore.edit { it.clear() }
}
