package com.plant.forestcare.ui.detail

data class PlantDetailUiState(
    val isLoading: Boolean = true,
    val plant: PlantDetailUi? = null,
    val errorMessage: String? = null,
    val isDeleting: Boolean = false,
    val deletedSuccessfully: Boolean = false
)

data class PlantDetailUi(
    val id: String,
    val name: String,
    val commonName: String,
    val scientificName: String,
    val description: String,
    val location: String,
    val sunlightExposure: String,
    val leafStatus: String,
    val soilHumidity: String,
    val currentWateringFrequency: String,
    val recommendedWateringDays: Int,
    val diagnosis: String,
    val diseaseName: String?,
    val diseaseProbability: Double?,
    val diseaseDescription: String?,
    val diseaseTreatmentRecommendation: String?,
    val geminiHealthStatus: String?,
    val geminiDiagnosis: String?,
    val geminiPossibleCauses: List<String>,
    val geminiWateringRecommendation: String?,
    val geminiLightRecommendation: String?,
    val geminiFertilizationRecommendation: String?,
    val geminiPruningRecommendation: String?,
    val geminiTreatmentRecommendation: String?,
    val geminiWeeklyCarePlan: List<String>,
    val geminiRiskLevel: String?,
    val treatmentRecommendation: String,
    val lightRecommendation: String,
    val fertilizationRecommendation: String,
    val pruningRecommendation: String,
    val nextWateringText: String,
    val nextReviewText: String,
    val tags: List<String>,
    val photoUri: String?,
    val healthStatus: String
)
