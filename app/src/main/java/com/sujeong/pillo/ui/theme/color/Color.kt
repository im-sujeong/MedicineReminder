package com.sujeong.pillo.ui.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Blue = Color(0xFF4276FD)
val Blue2 = Color(0xFF658FFD)
val Gray1 = Color(0xFFEEEEEE)
val Gray2 = Color(0xFFA4A4A4)
val Gray3 = Color(0xFF686868)
val Gray4 = Color(0xFF3F3F3F)
val Gray5 = Color(0xFF2D2D2D)
val Gray6 = Color(0xFF1C1B1F)
val White = Color(0xFFFFFFFF)
val Red = Color(0xFFFE5D5B)

val DarkColorScheme = PilloColor(
    material = darkColorScheme(
        primary = Blue,
        onPrimary = White,
        secondary = Blue2,
        onSecondary = White,
        surface = Gray5,
        onSurface = White,
        onSurfaceVariant = Gray2,
        error = Red
    ),
    iconDefault = Gray3,
    divider = Gray4
)

val LightColorScheme = PilloColor(
    material = lightColorScheme(
        primary = Blue,
        onPrimary = White,
        secondary = Blue2,
        onSecondary = White,
        surface = White,
        onSurface = Gray6,
        onSurfaceVariant = Gray3,
        error = Red
    ),
    iconDefault = Gray1,
    divider = Gray2
)