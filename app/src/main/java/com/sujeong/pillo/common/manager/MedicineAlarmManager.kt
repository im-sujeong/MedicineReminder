package com.sujeong.pillo.common.manager

interface MedicineAlarmManager {
    fun setAlarm(medicineId: Long, alarmDateTime: Long)
    fun cancelAlarm(medicineId: Long)
    fun clearAlarmNotification(medicineId: Long)
}