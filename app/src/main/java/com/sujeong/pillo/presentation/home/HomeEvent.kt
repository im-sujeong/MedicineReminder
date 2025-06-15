package com.sujeong.pillo.presentation.home

import java.time.LocalDate

sealed class HomeEvent {
    data class RequestNotificationPermission(
        val isCancel: Boolean = false
    ): HomeEvent()

    data class RequestSystemAlertWindowPermission(
        val isCancel: Boolean = false
    ): HomeEvent()

    data class OnNotificationPermissionResult(
        val isGranted: Boolean
    ) : HomeEvent()

    data class ChangedWeekPage(
        val page: Int
    ): HomeEvent()

    data class SelectedDate(
        val date: LocalDate
    ): HomeEvent()

    data object AddMedicine: HomeEvent()

    data class DeleteMedicine(
        val id: Long
    ): HomeEvent()
}