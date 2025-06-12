package com.sujeong.pillo.data.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class DataConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateTime {
        return LocalDateTime
            .ofInstant(
                Instant.ofEpochMilli(value),
                ZoneId.systemDefault()
            )
    }

    @TypeConverter
    fun dateToTimestamp(dateTime: LocalDateTime): Long {
        return dateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}