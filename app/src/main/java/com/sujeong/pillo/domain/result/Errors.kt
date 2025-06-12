package com.sujeong.pillo.domain.result

sealed class AlarmError: Throwable() {
    data object DuplicatedAlarmDate: AlarmError()
}