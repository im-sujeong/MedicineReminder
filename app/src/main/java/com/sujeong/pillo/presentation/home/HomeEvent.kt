package com.sujeong.pillo.presentation.home

import com.sujeong.pillo.presentation.home.model.CalendarDateModel

sealed class HomeEvent {
    data class SelectedDate(
        val date: CalendarDateModel
    ): HomeEvent()
}