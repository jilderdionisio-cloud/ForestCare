package com.plant.forestcare.core.config

import com.plant.forestcare.BuildConfig

object ApiConfig {
    const val PLANT_ID_BASE_URL = "https://plant.id/api/v3/"
    val PLANT_ID_API_KEY: String = BuildConfig.PLANT_ID_API_KEY

    const val PLANT_DISEASE_BASE_URL = "https://crop.kindwise.com/api/v1/"
    val PLANT_DISEASE_API_KEY: String = BuildConfig.PLANT_DISEASE_API_KEY

    const val PLANT_HEALTH_BASE_URL = "https://plant.id/api/v3/"
    const val PLANT_HEALTH_MODE = "health=all"

    const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"
    const val GEMINI_MODEL = "gemini-flash-latest"
    val GEMINI_API_KEY: String = BuildConfig.GEMINI_API_KEY
}
