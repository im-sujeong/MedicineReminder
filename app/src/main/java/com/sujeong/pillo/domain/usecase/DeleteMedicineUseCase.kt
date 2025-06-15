package com.sujeong.pillo.domain.usecase

import com.sujeong.pillo.domain.repository.MedicineRepository
import com.sujeong.pillo.domain.result.DataResult
import javax.inject.Inject

class DeleteMedicineUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository
) {
    suspend operator fun invoke(medicineId: Long) = runCatching {
        medicineRepository.deleteMedicine(medicineId)
    }.fold(
        onSuccess = {
            DataResult.Success(it)
        },
        onFailure = {
            DataResult.Error(it)
        }
    )
}