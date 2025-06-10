package com.sujeong.pillo.ui.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

data class PilloColor(
    val material: ColorScheme,
    val iconDefault: Color,
    val divider: Color,
) {
    val primary: Color = material.primary
    val onPrimary: Color = material.onPrimary
    val secondary: Color = material.secondary
    val background: Color = material.background
    val surface: Color = material.surface
    val onSurface: Color = material.onSurface
    val onSurfaceVariant: Color = material.onSurfaceVariant
    val error: Color = material.error
}