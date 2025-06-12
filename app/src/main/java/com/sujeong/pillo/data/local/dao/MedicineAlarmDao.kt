package com.sujeong.pillo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sujeong.pillo.data.local.model.MedicineAlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineAlarmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveMedicineAlarm(
        medicineAlarmEntity: MedicineAlarmEntity
    ): Long

    @Query("""
        SELECT * 
        FROM MedicineAlarmEntity 
        WHERE alarmDateTime >= :startMillis 
            AND alarmDateTime < :endOMillis 
        ORDER BY alarmDateTime ASC
    """)
    fun getMedicineAlarm(
        startMillis: Long,
        endOMillis: Long
    ): Flow<MedicineAlarmEntity?>

    @Query("DELETE FROM MedicineAlarmEntity WHERE id = :id")
    suspend fun deleteMedicineAlarm(id: Long)
}