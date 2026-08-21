package com.nexcard.nextwallet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.nexcard.nextwallet.domain.model.ThemeMode

private val Light = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = White,
    secondary = PurpleLight,
    background = GrayBackground,
    surface = White,
    error = ErrorRed,
)

private val Dark = darkColorScheme(
    primary = PurpleLight,
    secondary = PurplePrimary,
    background = BlackCard,
    surface = ColorTokens.DarkSurface,
    error = ErrorRed,
)

object ColorTokens {
    val DarkSurface = androidx.compose.ui.graphics.Color(0xFF212121)
}

@Composable
fun NextWalletTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit,
) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    MaterialTheme(
        colorScheme = if (dark) Dark else Light,
        typography = AppTypography,
        content = content,
    )
}
