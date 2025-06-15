package com.sujeong.pillo.domain.repository

import com.sujeong.pillo.domain.model.Medicine
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    fun getMedicine(
        startMillis: Long,
        endOMillis: Long
    ): Flow<Medicine?>

    suspend fun getMedicineById(
        id: Long
    ): Medicine?

    suspend fun saveMedicine(medicine: Medicine): Long

    suspend fun deleteMedicine(id: Long)

    suspend fun updateMedicine(medicine: Medicine)
}