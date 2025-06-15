package com.sujeong.pillo.domain.usecase

import com.sujeong.pillo.common.extension.millis
import com.sujeong.pillo.domain.model.Medicine
import com.sujeong.pillo.domain.repository.MedicineRepository
import com.sujeong.pillo.domain.result.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetMedicineUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository
) {
    suspend operator fun invoke(
        selectedDate: LocalDate
    ): Flow<DataResult<Medicine?>> = medicineRepository.getMedicine(
        startMillis = selectedDate.millis(),
        endOMillis = selectedDate.plusDays(1).millis()
    ).map<Medicine?, DataResult<Medicine?>> {
        DataResult.Success(it)
    }.catch {
        emit(DataResult.Error(it))
    }
}