package com.sujeong.pillo.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

@Serializable
data class AlarmRoute(
    val medicineId: Long
)