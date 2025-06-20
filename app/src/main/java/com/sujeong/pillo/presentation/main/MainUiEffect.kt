package com.sujeong.pillo.presentation.main

import com.sujeong.pillo.common.base.BaseUiEffect

sealed class MainUiEffect: BaseUiEffect {
    data object NavigateToHome: MainUiEffect()

    data class NavigateToAlarm(
        val medicineId: Long
    ): MainUiEffect()
}