package com.nexcard.nextwallet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
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

val LocalIsDarkTheme = staticCompositionLocalOf { false }

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
    val context = LocalContext.current
    val typography = remember(context) { appTypography(context) }
    CompositionLocalProvider(LocalIsDarkTheme provides dark) {
        MaterialTheme(
            colorScheme = if (dark) Dark else Light,
            typography = typography,
            content = content,
        )
    }
}
