package com.plant.forestcare.domain.model

data class DiseaseDiagnosisResult(
    val isHealthy: Boolean?,
    val diseaseName: String?,
    val probability: Double?,
    val description: String?,
    val treatmentRecommendation: String?
)
