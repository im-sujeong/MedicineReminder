package com.sujeong.pillo.domain.repository

interface AlarmRepository {
    fun setAlarm(medicineId: Long, alarmDateTime: Long)

    fun cancelAlarm(medicineId: Long)
}