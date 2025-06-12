package com.sujeong.pillo.domain.repository

import com.sujeong.pillo.domain.model.MedicineAlarm

interface MedicineAlarmRepository {
    suspend fun getMedicineAlarm(): MedicineAlarm
    suspend fun saveMedicineAlarm(medicineAlarm: MedicineAlarm): Long
    suspend fun deleteMedicineAlarm(id: Long)
}