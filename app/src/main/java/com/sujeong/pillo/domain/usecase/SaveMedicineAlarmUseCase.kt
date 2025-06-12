package com.sujeong.pillo.domain.usecase

import com.sujeong.pillo.domain.result.DataResult
import com.sujeong.pillo.domain.model.MedicineAlarm
import com.sujeong.pillo.domain.repository.MedicineAlarmRepository
import com.sujeong.pillo.domain.result.AlarmError
import java.time.LocalDateTime
import javax.inject.Inject

class SaveMedicineAlarmUseCase @Inject constructor(
    private val medicineAlarmRepository: MedicineAlarmRepository
) {
    suspend operator fun invoke(
        alarmDateTime: LocalDateTime,
        title: String = "약 ${System.currentTimeMillis()}",
    ) = runCatching {
        medicineAlarmRepository.saveMedicineAlarm(
            MedicineAlarm(
                title = title,
                alarmDateTime = alarmDateTime.withSecond(0).withNano(0),
            )
        ).let { alarmId ->
            if(alarmId > -1) {
                alarmId
            } else {
                throw AlarmError.DuplicatedAlarmDate
            }
        }
    }.fold(
        onSuccess = { DataResult.Success(it) },
        onFailure = { DataResult.Error(it) }
    )
}