package com.plant.forestcare.domain.model

data class PlantApisConnectionTestResult(
    val identification: PlantApiTestResult,
    val cropHealth: PlantApiTestResult,
    val plantHealth: PlantApiTestResult
)

data class PlantApiTestResult(
    val serviceName: String,
    val success: Boolean,
    val httpCode: Int?,
    val message: String,
    val responsePreview: String? = null
)
