package com.sujeong.pillo.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sujeong.pillo.domain.model.enums.AlarmStatus
import java.time.LocalDateTime

@Entity(
    indices = [Index(value = ["alarmDateTime"], unique = true)]
)
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val alarmDateTime: LocalDateTime,
    val status: AlarmStatus,
    val takenAt: LocalDateTime?
)