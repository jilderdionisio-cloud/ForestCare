package com.plant.forestcare.ui.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PlantCareColorScheme = lightColorScheme(
    primary = PlantCareColors.PrimaryGreen,
    secondary = PlantCareColors.SecondaryGreen,
    tertiary = PlantCareColors.Accent,
    background = PlantCareColors.Background,
    surface = PlantCareColors.Surface,
    error = PlantCareColors.Danger,
    onPrimary = PlantCareColors.Surface,
    onSecondary = PlantCareColors.TextPrimary,
    onBackground = PlantCareColors.TextPrimary,
    onSurface = PlantCareColors.TextPrimary,
    onError = PlantCareColors.Surface
)

@Composable
fun PlantCareTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PlantCareColorScheme,
        typography = PlantCareTypography,
        content = content
    )
}
