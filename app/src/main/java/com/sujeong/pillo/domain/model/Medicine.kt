package com.sujeong.pillo.domain.model

import android.os.Parcelable
import com.sujeong.pillo.domain.model.enums.AlarmStatus
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Medicine(
    val id: Long = 0,
    val title: String,
    val alarmDateTime: LocalDateTime,
    val alarmStatus: AlarmStatus = AlarmStatus.SCHEDULED,
    val takenAt: LocalDateTime? = null
) : Parcelable
