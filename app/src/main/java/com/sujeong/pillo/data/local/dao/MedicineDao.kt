package com.sujeong.pillo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sujeong.pillo.data.local.model.MedicineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveMedicine(
        medicineEntity: MedicineEntity
    ): Long

    @Query(
        """
        SELECT * 
        FROM MedicineEntity 
        WHERE alarmDateTime >= :startMillis 
            AND alarmDateTime < :endOMillis 
        ORDER BY alarmDateTime ASC
    """
    )
    fun getMedicine(
        startMillis: Long,
        endOMillis: Long
    ): Flow<MedicineEntity?>

    @Query("SELECT * FROM MedicineEntity WHERE id = :id")
    suspend fun getMedicineById(id: Long): MedicineEntity?

    @Query("DELETE FROM MedicineEntity WHERE id = :id")
    suspend fun deleteMedicine(id: Long)

    @Update
    suspend fun updateMedicine(medicineEntity: MedicineEntity)
}