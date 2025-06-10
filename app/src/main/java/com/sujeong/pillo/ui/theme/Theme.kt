package com.sujeong.pillo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.sujeong.pillo.ui.theme.color.DarkColorScheme
import com.sujeong.pillo.ui.theme.color.LightColorScheme
import com.sujeong.pillo.ui.theme.typography.Typography

val LocalColorScheme = staticCompositionLocalOf { LightColorScheme }

@Composable
fun PilloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if(darkTheme) {
        DarkColorScheme
    }else {
        LightColorScheme
    }

    CompositionLocalProvider(
        LocalColorScheme provides colorScheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme.material,
            typography = Typography.typography,
            content = content
        )
    }
}