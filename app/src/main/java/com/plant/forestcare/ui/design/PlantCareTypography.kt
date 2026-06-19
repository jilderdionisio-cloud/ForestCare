package com.plant.forestcare.ui.design

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object PlantCareTextStyles {
    val Title = TextStyle(fontSize = 24.sp, lineHeight = 30.sp, fontWeight = FontWeight.SemiBold)
    val Subtitle = TextStyle(fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium)
    val CardTitle = TextStyle(fontSize = 16.sp, lineHeight = 21.sp, fontWeight = FontWeight.SemiBold)
    val Body = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal)
    val Secondary = TextStyle(fontSize = 12.sp, lineHeight = 17.sp, fontWeight = FontWeight.Normal)
    val State = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
}

val PlantCareTypography = Typography(
    headlineMedium = PlantCareTextStyles.Title,
    titleMedium = PlantCareTextStyles.Subtitle,
    titleSmall = PlantCareTextStyles.CardTitle,
    bodyMedium = PlantCareTextStyles.Body,
    bodySmall = PlantCareTextStyles.Secondary,
    labelMedium = PlantCareTextStyles.State
)
