package com.sujeong.pillo.data.repository

import com.sujeong.pillo.data.local.dao.MedicineDao
import com.sujeong.pillo.data.mapper.toDomain
import com.sujeong.pillo.data.mapper.toEntity
import com.sujeong.pillo.domain.model.Medicine
import com.sujeong.pillo.domain.repository.MedicineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
    private val medicineDao: MedicineDao
): MedicineRepository {
    override fun getMedicine(
        startMillis: Long,
        endOMillis: Long
    ): Flow<Medicine?> {
        return medicineDao.getMedicine(
            startMillis, endOMillis
        ).map {
            it?.toDomain()
        }
    }

    override suspend fun getMedicineById(id: Long): Medicine? {
        return medicineDao.getMedicineById(id)?.toDomain()
    }

    override suspend fun saveMedicine(medicine: Medicine): Long {
        return medicineDao.saveMedicine(
            medicine.toEntity()
        )
    }

    override suspend fun deleteMedicine(id: Long) {
        medicineDao.deleteMedicine(id)
    }

    override suspend fun updateMedicine(medicine: Medicine) {
        medicineDao.updateMedicine(medicine.toEntity())
    }
}