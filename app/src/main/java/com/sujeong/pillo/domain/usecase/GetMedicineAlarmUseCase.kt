package com.sujeong.pillo.domain.usecase

import com.sujeong.pillo.common.extension.millis
import com.sujeong.pillo.domain.model.MedicineAlarm
import com.sujeong.pillo.domain.repository.MedicineAlarmRepository
import com.sujeong.pillo.domain.result.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetMedicineAlarmUseCase @Inject constructor(
    private val medicineAlarmRepository: MedicineAlarmRepository
) {
    suspend operator fun invoke(
        selectedDate: LocalDate
    ): Flow<DataResult<MedicineAlarm?>> = medicineAlarmRepository.getMedicineAlarm(
        startMillis = selectedDate.millis(),
        endOMillis = selectedDate.plusDays(1).millis()
    ).map<MedicineAlarm?, DataResult<MedicineAlarm?>> {
        DataResult.Success(it)
    }.catch {
        emit(DataResult.Error(it))
    }
}