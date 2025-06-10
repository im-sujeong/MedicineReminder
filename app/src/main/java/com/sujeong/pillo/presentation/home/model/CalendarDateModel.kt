package com.sujeong.pillo.presentation.home.model

import java.time.LocalDate

data class CalendarDateModel(
    val date: LocalDate,
    val isToday: Boolean,
    val isSelected: Boolean
)