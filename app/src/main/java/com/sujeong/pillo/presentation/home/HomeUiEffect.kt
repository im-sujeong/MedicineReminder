package com.sujeong.pillo.presentation.home

import com.sujeong.pillo.presentation.base.BaseUiEffect

sealed class HomeUiEffect: BaseUiEffect {
    data class ShowMessage(
        val message: String
    ): HomeUiEffect()
}