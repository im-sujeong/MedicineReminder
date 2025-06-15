package com.sujeong.pillo.presentation.alarm

import com.sujeong.pillo.common.base.BaseUiEffect

sealed class AlarmUiEffect: BaseUiEffect {
    data object TakenMedicine: AlarmUiEffect()
    data object SkippedMedicine: AlarmUiEffect()
}