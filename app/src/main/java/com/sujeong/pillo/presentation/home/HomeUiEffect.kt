package com.sujeong.pillo.presentation.home

import com.sujeong.pillo.common.base.BaseUiEffect

sealed class HomeUiEffect: BaseUiEffect {
    data object RequestNotificationPermission: HomeUiEffect()

    data object RequestSystemAlertWindowPermission: HomeUiEffect()

    data class ShowMessage(
        val message: String
    ): HomeUiEffect()
}