package com.nexcard.nextwallet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nexcard.nextwallet.ui.navigation.NextWalletNavHost
import com.nexcard.nextwallet.ui.theme.NextWalletTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel = hiltViewModel<AppViewModel>()
            val themeMode by appViewModel.themeMode.collectAsStateWithLifecycle()
            val sessionActive by appViewModel.sessionActive.collectAsStateWithLifecycle()
            val notificationsEnabled by appViewModel.notificationsEnabled.collectAsStateWithLifecycle()

            NextWalletTheme(themeMode = themeMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NextWalletNavHost(
                        isSessionActive = sessionActive,
                        themeMode = themeMode,
                        notificationsEnabled = notificationsEnabled,
                        onThemeChanged = appViewModel::setTheme,
                        onNotificationsChanged = appViewModel::setNotifications,
                    )
                }
            }
        }
    }
}
