package com.sujeong.pillo

sealed class MainEvent {
    data class Initialize(
        val medicineId: Long
    ): MainEvent()

    data object ClearAlarm: MainEvent()
}