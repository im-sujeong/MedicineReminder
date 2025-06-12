package com.sujeong.pillo.domain.usecase

import com.sujeong.pillo.domain.repository.MedicineAlarmRepository
import com.sujeong.pillo.domain.result.DataResult
import javax.inject.Inject

class DeleteMedicineAlarmUseCase @Inject constructor(
    private val medicineAlarmRepository: MedicineAlarmRepository
) {
    suspend operator fun invoke(alarmId: Long) = runCatching {
        medicineAlarmRepository.deleteMedicineAlarm(alarmId)
    }.fold(
        onSuccess = {
            DataResult.Success(it)
        },
        onFailure = {
            DataResult.Error(it)
        }
    )
}