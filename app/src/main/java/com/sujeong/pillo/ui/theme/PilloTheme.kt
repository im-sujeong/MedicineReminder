package com.sujeong.pillo.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.sujeong.pillo.ui.theme.color.PilloColor
import com.sujeong.pillo.ui.theme.typography.PilloTypography
import com.sujeong.pillo.ui.theme.typography.Typography

object PilloTheme {
    val colors: PilloColor
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography : PilloTypography
        @Composable
        @ReadOnlyComposable
        get() = Typography
}