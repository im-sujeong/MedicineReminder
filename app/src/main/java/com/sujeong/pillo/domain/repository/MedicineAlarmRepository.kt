package com.sujeong.pillo.domain.repository

import com.sujeong.pillo.domain.model.MedicineAlarm
import kotlinx.coroutines.flow.Flow

interface MedicineAlarmRepository {
    suspend fun getMedicineAlarm(
        startMillis: Long,
        endOMillis: Long
    ): Flow<MedicineAlarm?>
    suspend fun saveMedicineAlarm(medicineAlarm: MedicineAlarm): Long
    suspend fun deleteMedicineAlarm(id: Long)
}