package com.plant.forestcare.domain.model

data class GeminiPlantAnalysisInput(
    val imageBase64: String,
    val commonName: String,
    val scientificName: String,
    val plantType: String? = null,
    val identificationConfidence: Double? = null,
    val diseaseDiagnosis: DiseaseDiagnosisResult? = null,
    val locationType: String,
    val lightExposure: String,
    val leafStatus: String,
    val soilHumidity: String,
    val currentWateringFrequency: String
)

data class PlantAnalysisResult(
    val healthStatus: String = "",
    val diagnosis: String = "",
    val possibleCauses: List<String> = emptyList(),
    val wateringRecommendation: String = "",
    val lightRecommendation: String = "",
    val fertilizationRecommendation: String = "",
    val pruningRecommendation: String = "",
    val treatmentRecommendation: String = "",
    val monthlyPlan: List<String> = emptyList()
) {
    val weeklyCarePlan: List<String>
        get() = monthlyPlan

    val riskLevel: String
        get() = when {
            healthStatus.contains("urgente", ignoreCase = true) ||
                healthStatus.contains("alto", ignoreCase = true) -> "alto"
            healthStatus.contains("atencion", ignoreCase = true) ||
                healthStatus.contains("atención", ignoreCase = true) ||
                healthStatus.contains("medio", ignoreCase = true) -> "medio"
            else -> "bajo"
    }
}

typealias GeminiPlantAnalysisResult = PlantAnalysisResult

class GeminiPlantAnalysisException(
    val code: Int?,
    val errorBody: String,
    override val message: String
) : Exception(message)
