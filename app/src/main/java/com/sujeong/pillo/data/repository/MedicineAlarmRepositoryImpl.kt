package com.sujeong.pillo.data.repository

import com.sujeong.pillo.data.local.dao.MedicineAlarmDao
import com.sujeong.pillo.data.mapper.toEntity
import com.sujeong.pillo.domain.model.MedicineAlarm
import com.sujeong.pillo.domain.repository.MedicineAlarmRepository
import javax.inject.Inject

class MedicineAlarmRepositoryImpl @Inject constructor(
    private val medicineAlarmDao: MedicineAlarmDao
): MedicineAlarmRepository {
    override suspend fun getMedicineAlarm(): MedicineAlarm {
        TODO("Not yet implemented")
    }

    override suspend fun saveMedicineAlarm(medicineAlarm: MedicineAlarm): Long {
        return medicineAlarmDao.saveMedicineAlarm(
            medicineAlarm.toEntity()
        )
    }

    override suspend fun deleteMedicineAlarm(id: Long) {
        TODO("Not yet implemented")
    }
}