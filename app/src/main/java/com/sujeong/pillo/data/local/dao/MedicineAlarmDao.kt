package com.sujeong.pillo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.sujeong.pillo.data.local.model.MedicineAlarmEntity

@Dao
interface MedicineAlarmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveMedicineAlarm(
        medicineAlarmEntity: MedicineAlarmEntity
    ): Long
}