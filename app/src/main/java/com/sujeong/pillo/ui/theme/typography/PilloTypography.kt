package com.sujeong.pillo.ui.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class PilloTypography(
    val typography: Typography = Typography(
        displayLarge = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 36.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        )
    ),
    val bodyMediumBold: TextStyle = typography.bodyMedium.copy(
        fontWeight = FontWeight.Bold
    ),
    val labelLargeBold: TextStyle = typography.labelLarge.copy(
        fontWeight = FontWeight.Bold
    )
) {
    val displayLarge = typography.displayLarge
    val displayMedium = typography.displayMedium
    val titleLarge = typography.titleLarge
    val bodyMedium = typography.bodyMedium
    val labelLarge = typography.labelLarge
}