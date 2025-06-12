package com.sujeong.pillo.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sujeong.pillo.R
import com.sujeong.pillo.domain.model.enums.AlarmStatus

@Composable
fun AlarmStatus.toUiText(
    vararg args: Any
): String {
    return when(this) {
        AlarmStatus.SCHEDULED -> stringResource(R.string.label_alarm_status_scheduled)
        AlarmStatus.TAKEN -> stringResource(
            R.string.label_alarm_status_taken,
            *args
        )
        AlarmStatus.SKIPPED -> stringResource(R.string.label_alarm_status_skipped)
    }
}