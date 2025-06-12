package com.sujeong.pillo.domain.model

import com.sujeong.pillo.domain.model.enums.AlarmStatus
import java.time.LocalDateTime

data class MedicineAlarm(
    val id: Long,
    val title: String,
    val alarmDateTime: LocalDateTime,
    val alarmStatus: AlarmStatus,
    val takenAt: LocalDateTime?
)
