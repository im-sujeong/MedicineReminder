package com.sujeong.pillo.domain.model

import com.sujeong.pillo.domain.model.enums.AlarmStatus
import java.time.LocalDateTime

data class MedicineAlarm(
    val id: Long = 0,
    val title: String,
    val alarmDateTime: LocalDateTime,
    val alarmStatus: AlarmStatus = AlarmStatus.SCHEDULED,
    val takenAt: LocalDateTime? = null
)
