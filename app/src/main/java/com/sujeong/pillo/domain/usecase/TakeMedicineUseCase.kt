package com.sujeong.pillo.domain.usecase

import com.sujeong.pillo.domain.model.Medicine
import com.sujeong.pillo.domain.model.enums.AlarmStatus
import com.sujeong.pillo.domain.repository.MedicineRepository
import com.sujeong.pillo.domain.result.DataResult
import java.time.LocalDateTime
import javax.inject.Inject

class TakeMedicineUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository
) {
    suspend operator fun invoke(medicine: Medicine) = runCatching {
        medicineRepository.updateMedicine(
            medicine.copy(
                alarmStatus = AlarmStatus.TAKEN,
                takenAt = LocalDateTime.now()
            )
        )
    }.fold(
        onSuccess = { DataResult.Success(it) },
        onFailure = { DataResult.Error(it) }
    )
}