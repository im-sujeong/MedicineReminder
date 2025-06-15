package com.sujeong.pillo.presentation.home

import com.sujeong.pillo.domain.model.Medicine
import com.sujeong.pillo.presentation.home.model.CalendarDateModel
import com.sujeong.pillo.presentation.home.model.CalendarWeekModel
import com.sujeong.pillo.presentation.home.model.PermissionDialogType

data class HomeState(
    val showPermissionDialogType: PermissionDialogType = PermissionDialogType.NOTIFICATION,
    val isShowSystemWindowPermission: Boolean = false,
    val currentWeekPage: Int = 1,
    val weeks: List<CalendarWeekModel>,
    val selectedDate: CalendarDateModel,
    val medicine: Medicine? = null
)