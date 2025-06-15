package com.sujeong.pillo.presentation.main

sealed class MainEvent {
    data class Initialize(
        val medicineId: Long
    ): MainEvent()

    data object ClearAlarm: MainEvent()
}