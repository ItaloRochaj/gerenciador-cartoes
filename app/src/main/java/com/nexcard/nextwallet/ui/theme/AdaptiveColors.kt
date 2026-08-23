package com.nexcard.nextwallet.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun darkAwareTextColor(lightColor: Color): Color =
    if (LocalIsDarkTheme.current) Color.White else lightColor

