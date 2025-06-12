package com.sujeong.pillo.domain.usecase

import com.sujeong.pillo.domain.result.Result
import com.sujeong.pillo.domain.model.MedicineAlarm
import com.sujeong.pillo.domain.model.enums.AlarmStatus
import com.sujeong.pillo.domain.repository.MedicineAlarmRepository
import com.sujeong.pillo.domain.result.AlarmError
import java.time.LocalDateTime
import javax.inject.Inject

class SaveMedicineAlarmUseCase @Inject constructor(
    private val medicineAlarmRepository: MedicineAlarmRepository
) {
    suspend operator fun invoke(
        alarmDateTime: LocalDateTime,
        title: String = "약 이름",
    ) = runCatching {
        medicineAlarmRepository.saveMedicineAlarm(
            MedicineAlarm(
                id = 0,
                title = title,
                alarmDateTime = alarmDateTime.withSecond(0).withNano(0),
                alarmStatus = AlarmStatus.SCHEDULED,
                takenAt = null
            )
        ).let { alarmId ->
            if(alarmId > -1) {
                Result.Success(Unit)
            } else {
                Result.Error(AlarmError.DuplicatedAlarmDate)
            }
        }
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Error(it) }
    )
}