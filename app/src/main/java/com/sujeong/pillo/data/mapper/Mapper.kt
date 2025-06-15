package com.sujeong.pillo.data.mapper

import com.sujeong.pillo.data.local.model.MedicineEntity
import com.sujeong.pillo.domain.model.Medicine

fun Medicine.toEntity(): MedicineEntity {
    return MedicineEntity(
        id = id,
        title = title,
        alarmDateTime = alarmDateTime,
        status = alarmStatus,
        takenAt = takenAt
    )
}

fun MedicineEntity.toDomain(): Medicine {
    return Medicine(
        id = id,
        title = title,
        alarmDateTime = alarmDateTime,
        alarmStatus = status,
        takenAt = takenAt
    )
}