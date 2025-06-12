package com.sujeong.pillo.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sujeong.pillo.domain.model.enums.AlarmStatus
import com.sujeong.pillo.domain.model.enums.AlarmStatus.*
import com.sujeong.pillo.ui.theme.PilloTheme

@Composable
fun AlarmStatus.toUiColor(): Color {
    return when(this) {
        SCHEDULED -> PilloTheme.colors.onSurfaceVariant
        TAKEN -> PilloTheme.colors.primary
        SKIPPED -> PilloTheme.colors.error
    }
}