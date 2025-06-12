package com.sujeong.pillo.presentation.home

import com.sujeong.pillo.domain.model.MedicineAlarm
import com.sujeong.pillo.presentation.home.model.CalendarDateModel
import com.sujeong.pillo.presentation.home.model.CalendarWeekModel

data class HomeState(
    val weeks: List<CalendarWeekModel>,
    val selectedPage: Int = 1,
    val selectedDate: CalendarDateModel,
    val medicineAlarm: MedicineAlarm? = null
)