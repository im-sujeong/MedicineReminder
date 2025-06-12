package com.sujeong.pillo.presentation.home

import java.time.LocalDate

sealed class HomeEvent {
    data class SelectedDate(
        val date: LocalDate
    ): HomeEvent()

    data object AddMedicineAlarm: HomeEvent()

    data class DeleteMedicineAlarm(
        val id: Long
    ): HomeEvent()
}