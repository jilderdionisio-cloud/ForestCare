package com.plant.forestcare.ui.form

import com.plant.forestcare.domain.model.PlantIdentificationResult
import com.plant.forestcare.domain.care.CarePlan
import com.plant.forestcare.domain.model.DiseaseDiagnosisResult
import com.plant.forestcare.domain.model.GeminiPlantAnalysisResult

data class PlantFormUiState(
    val editingPlantId: String? = null,
    val isEditMode: Boolean = false,
    val createdAt: Long? = null,
    val customName: String = "",
    val commonName: String = "",
    val scientificName: String = "",
    val description: String = "",
    val growthLocation: String = "Interior",
    val sunlightExposure: String = "Luz indirecta",
    val leafStatus: String = "Sanas",
    val soilHumidity: String = "No sé",
    val currentWateringFrequency: String = "No recuerdo",
    val photoUri: String? = null,
    val tags: List<String> = listOf("Apta para mascotas", "Purificadora de aire"),
    val selectedImageUri: String? = null,
    val identificationResult: PlantIdentificationResult? = null,
    val isIdentifying: Boolean = false,
    val identificationSuccess: Boolean = false,
    val confidence: Double? = null,
    val identificationMessage: String? = null,
    val identificationErrorMessage: String? = null,
    val diseaseDiagnosisResult: DiseaseDiagnosisResult? = null,
    val isDiseaseAnalyzing: Boolean = false,
    val diseaseAnalysisMessage: String? = null,
    val diseaseAnalysisErrorMessage: String? = null,
    val geminiAnalysisResult: GeminiPlantAnalysisResult? = null,
    val isGeminiAnalyzing: Boolean = false,
    val geminiAnalysisMessage: String? = null,
    val geminiAnalysisErrorMessage: String? = null,
    val carePlan: CarePlan? = null,
    val carePlanGenerated: Boolean = false,
    val isLoading: Boolean = false,
    val isAnalyzing: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val savedSuccessfully: Boolean = false
) {
    val analysisResult: PlantIdentificationResult?
        get() = identificationResult

    val generatedCarePlan: CarePlan?
        get() = carePlan
}
