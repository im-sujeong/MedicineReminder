package com.sujeong.pillo.domain.usecase

import com.sujeong.pillo.domain.result.DataResult
import com.sujeong.pillo.domain.model.Medicine
import com.sujeong.pillo.domain.repository.MedicineRepository
import com.sujeong.pillo.domain.result.AlarmError
import java.time.LocalDateTime
import javax.inject.Inject

class SaveMedicineUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository
) {
    suspend operator fun invoke(
        alarmDateTime: LocalDateTime,
        title: String = "약 ${System.currentTimeMillis()}",
    ) = runCatching {
        medicineRepository.saveMedicine(
            Medicine(
                title = title,
                alarmDateTime = alarmDateTime.withSecond(0).withNano(0),
            )
        ).let { medicineId ->
            if(medicineId > -1) {
                medicineId
            } else {
                throw AlarmError.DuplicatedAlarmDate
            }
        }
    }.fold(
        onSuccess = { DataResult.Success(it) },
        onFailure = { DataResult.Error(it) }
    )
}