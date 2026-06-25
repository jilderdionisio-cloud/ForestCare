package com.plant.forestcare.ui.design

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

object PlantCareAnimations {
    const val Fast = 200
    const val Normal = 300
    const val Slow = 400

    fun <T> standard(durationMillis: Int = Normal) = tween<T>(
        durationMillis = durationMillis,
        easing = FastOutSlowInEasing
    )
}
