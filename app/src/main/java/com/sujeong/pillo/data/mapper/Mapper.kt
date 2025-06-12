package com.sujeong.pillo.data.mapper

import com.sujeong.pillo.data.local.model.MedicineAlarmEntity
import com.sujeong.pillo.domain.model.MedicineAlarm

fun MedicineAlarm.toEntity(): MedicineAlarmEntity {
    return MedicineAlarmEntity(
        id = id,
        title = title,
        alarmDateTime = alarmDateTime,
        status = alarmStatus,
        takenAt = takenAt
    )
}

fun MedicineAlarmEntity.toDomain(): MedicineAlarm {
    return MedicineAlarm(
        id = id,
        title = title,
        alarmDateTime = alarmDateTime,
        alarmStatus = status,
        takenAt = takenAt
    )
}