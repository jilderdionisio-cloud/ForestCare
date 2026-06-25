package com.plant.forestcare.domain.care

import com.plant.forestcare.domain.model.DiseaseDiagnosisResult

data class PlantCondition(
    val commonName: String,
    val scientificName: String,
    val locationType: String,
    val lightExposure: String,
    val leafStatus: String,
    val soilHumidity: String,
    val currentWateringFrequency: String,
    val apiDescription: String? = null,
    val diseaseDiagnosis: DiseaseDiagnosisResult? = null
)
