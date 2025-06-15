package com.sujeong.pillo.presentation.alarm

sealed class AlarmEvent {
    data object TakeMedicine: AlarmEvent()
    data object SkipMedicine: AlarmEvent()
}