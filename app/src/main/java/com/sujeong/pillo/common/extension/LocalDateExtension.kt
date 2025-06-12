package com.sujeong.pillo.common.extension

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.toString(
    pattern: String
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return format(formatter)
}

fun LocalDate.millis() = this.atStartOfDay(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()

fun LocalDateTime.toString(
    pattern: String
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return format(formatter)
}