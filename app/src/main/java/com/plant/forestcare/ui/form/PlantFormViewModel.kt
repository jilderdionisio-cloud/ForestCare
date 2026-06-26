package com.plant.forestcare.ui.form

import android.app.Application
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plant.forestcare.data.PlantIdApiException
import com.plant.forestcare.data.PlantRepository
import com.plant.forestcare.data.local.PlantEntity
import com.plant.forestcare.domain.care.GeminiPlantAnalysisUseCase
import com.plant.forestcare.domain.care.PlantCareRecommendationEngine
import com.plant.forestcare.domain.care.PlantCondition
import com.plant.forestcare.domain.model.DiseaseDiagnosisResult
import com.plant.forestcare.domain.model.GeminiPlantAnalysisInput
import com.plant.forestcare.domain.model.GeminiPlantAnalysisResult
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

@HiltViewModel
class PlantFormViewModel @Inject constructor(
    application: Application,
    private val repository: PlantRepository,
    private val recommendationEngine: PlantCareRecommendationEngine,
    private val geminiAnalysisUseCase: GeminiPlantAnalysisUseCase
) : ViewModel() {
    
    private val contentResolver = application.contentResolver

    private val _uiState = MutableStateFlow(PlantFormUiState())
    val uiState: StateFlow<PlantFormUiState> = _uiState.asStateFlow()

    fun loadPlantForEdit(plantId: String?) {
        if (plantId.isNullOrBlank() || _uiState.value.editingPlantId == plantId) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            runCatching {
                withContext(Dispatchers.IO) { repository.getPlantById(plantId).first() }
            }.onSuccess { plant ->
                if (plant == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No encontramos esta planta para editar"
                        )
                    }
                } else {
                    _uiState.value = PlantFormUiState(
                        editingPlantId = plant.id,
                        isEditMode = true,
                        createdAt = plant.createdAt,
                        customName = plant.customName,
                        commonName = plant.commonName,
                        scientificName = plant.scientificName,
                        description = plant.description,
                        growthLocation = plant.locationType,
                        sunlightExposure = plant.lightExposure,
                        leafStatus = plant.leafStatus,
                        soilHumidity = plant.soilHumidity,
                        currentWateringFrequency = plant.currentWateringFrequency,
                        photoUri = plant.photoUri,
                        tags = plant.tags.split(",").map { tag -> tag.trim() }.filter { tag -> tag.isNotEmpty() },
                        selectedImageUri = plant.photoUri,
                        diseaseDiagnosisResult = DiseaseDiagnosisResult(
                            isHealthy = plant.isHealthyByDiseaseApi,
                            diseaseName = plant.diseaseName,
                            probability = plant.diseaseProbability,
                            description = plant.diseaseDescription,
                            treatmentRecommendation = plant.diseaseTreatmentRecommendation
                        ),
                        geminiAnalysisResult = plant.toGeminiAnalysisResult(),
                        carePlan = com.plant.forestcare.domain.care.CarePlan(
                            diagnosis = plant.geminiDiagnosis ?: plant.diagnosis,
                            recommendedWateringDays = plant.recommendedWateringDays,
                            wateringRecommendation = "Riega cada ${plant.recommendedWateringDays} días.",
                            lightRecommendation = plant.geminiLightRecommendation ?: plant.lightRecommendation,
                            fertilizationRecommendation = plant.geminiFertilizationRecommendation ?: plant.fertilizationRecommendation,
                            pruningRecommendation = plant.geminiPruningRecommendation ?: plant.pruningRecommendation,
                            treatmentRecommendation = plant.geminiTreatmentRecommendation ?: plant.treatmentRecommendation,
                            nextReviewDays = ((plant.nextReviewAt - System.currentTimeMillis()) / (24L * 60L * 60L * 1000L)).toInt().coerceAtLeast(0),
                            urgency = com.plant.forestcare.domain.care.CareUrgency.fromStorage(plant.healthStatus)
                        ),
                        carePlanGenerated = true,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo cargar la planta"
                    )
                }
            }
        }
    }

    fun onCustomNameChange(value: String) {
        _uiState.update { it.copy(customName = value, errorMessage = null, successMessage = null) }
    }

    fun onCommonNameChange(value: String) {
        _uiState.update { it.copy(commonName = value, errorMessage = null, successMessage = null) }
    }

    fun onScientificNameChange(value: String) {
        _uiState.update { it.copy(scientificName = value, errorMessage = null, successMessage = null) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value, errorMessage = null, successMessage = null) }
    }

    fun onGrowthLocationChange(value: String) {
        _uiState.update { it.copy(growthLocation = value, carePlanGenerated = false) }
    }

    fun onSunlightExposureChange(value: String) {
        _uiState.update { it.copy(sunlightExposure = value, carePlanGenerated = false) }
    }

    fun onLeafStatusChange(value: String) {
        _uiState.update { it.copy(leafStatus = value, carePlanGenerated = false) }
    }

    fun onSoilHumidityChange(value: String) {
        _uiState.update { it.copy(soilHumidity = value, carePlanGenerated = false) }
    }

    fun onCurrentWateringFrequencyChange(value: String) {
        _uiState.update { it.copy(currentWateringFrequency = value, carePlanGenerated = false) }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update {
            it.copy(
                selectedImageUri = uri?.toString(),
                photoUri = uri?.toString(),
                identificationResult = null,
                identificationSuccess = false,
                confidence = null,
                identificationMessage = null,
                identificationErrorMessage = null,
                diseaseDiagnosisResult = null,
                diseaseAnalysisMessage = null,
                diseaseAnalysisErrorMessage = null,
                geminiAnalysisResult = null,
                geminiAnalysisMessage = null,
                geminiAnalysisErrorMessage = null,
                saveSuccess = false,
                savedSuccessfully = false
            )
        }
    }

    fun onTakePhoto(uri: Uri?) {
        onImageSelected(uri)
    }

    fun identifySelectedPlant() {
        val imageUri = _uiState.value.selectedImageUri
        if (imageUri.isNullOrBlank()) {
            _uiState.update { it.copy(identificationErrorMessage = "Selecciona una imagen válida.") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAnalyzing = true,
                    isIdentifying = true,
                    isDiseaseAnalyzing = true,
                    identificationErrorMessage = null,
                    identificationMessage = null,
                    diseaseAnalysisErrorMessage = null,
                    diseaseAnalysisMessage = null
                )
            }
            val imageBase64 = runCatching { imageUri.toBase64() }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(
                            isIdentifying = false,
                            isDiseaseAnalyzing = false,
                            isAnalyzing = false,
                            identificationErrorMessage = "Imagen inválida."
                        )
                    }
                }
                .getOrNull() ?: return@launch

            runImageAnalysis(imageBase64)
        }
    }

    private fun applyIdentificationResult(result: com.plant.forestcare.domain.model.PlantIdentificationResult) {
        _uiState.update {
            it.copy(
                commonName = result.commonName,
                scientificName = result.scientificName,
                description = result.description ?: "Información sugerida para tu planta.",
                identificationResult = result,
                isIdentifying = false,
                identificationSuccess = true,
                confidence = result.confidence,
                identificationMessage = "Creemos que es esta planta",
                identificationErrorMessage = null,
                carePlanGenerated = false
            )
        }
    }

    fun onAddTag(tag: String = "Nueva etiqueta") {
        _uiState.update { state ->
            if (state.tags.contains(tag)) state else state.copy(tags = state.tags + tag)
        }
    }

    fun onRemoveTag(tag: String) {
        _uiState.update { it.copy(tags = it.tags.filterNot { currentTag -> currentTag == tag }) }
    }

    fun generateCarePlan() {
        val state = _uiState.value
        val carePlan = recommendationEngine.generateCarePlan(state.toCondition())
        _uiState.update {
            it.copy(
                carePlan = carePlan,
                carePlanGenerated = true,
                errorMessage = null,
                successMessage = null
            )
        }
        val imageUri = state.selectedImageUri ?: state.photoUri
        if (!imageUri.isNullOrBlank()) {
            viewModelScope.launch {
                val imageBase64 = runCatching { imageUri.toBase64() }.getOrNull()
                if (imageBase64 != null) {
                    analyzeWithGemini(imageBase64)
                }
            }
        }
    }

    fun savePlant() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isSaving = true,
                    errorMessage = null,
                    successMessage = null,
                    saveSuccess = false,
                    savedSuccessfully = false
                )
            }

            val initialState = _uiState.value
            val imageUri = initialState.selectedImageUri ?: initialState.photoUri
            if (
                !imageUri.isNullOrBlank() &&
                initialState.identificationResult == null &&
                initialState.diseaseDiagnosisResult == null &&
                initialState.geminiAnalysisResult == null
            ) {
                val imageBase64 = runCatching { imageUri.toBase64() }
                    .onFailure {
                        Log.w(TAG, "[FLOW][PLANT_ID] Imagen inválida antes de guardar: ${it.message}")
                        _uiState.update { state ->
                            state.copy(identificationErrorMessage = "Imagen inválida. Puedes continuar con registro manual.")
                        }
                    }
                    .getOrNull()
                if (imageBase64 != null) {
                    runImageAnalysis(imageBase64)
                }
            }

            val state = _uiState.value
            if (
                state.customName.isBlank() ||
                state.commonName.isBlank() ||
                state.growthLocation.isBlank() ||
                state.sunlightExposure.isBlank()
            ) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaving = false,
                        errorMessage = "Completa los campos obligatorios"
                    )
                }
                return@launch
            }

            val now = System.currentTimeMillis()
            val localCarePlan = state.carePlan ?: recommendationEngine.generateCarePlan(state.toCondition())
            Log.d(TAG, "[FLOW][CARE_PLAN] Plan local generado=${state.carePlan == null}, geminiDisponible=${state.geminiAnalysisResult != null}")
            val carePlan = recommendationEngine.applyGeminiAnalysis(localCarePlan, state.geminiAnalysisResult)
            val disease = state.diseaseDiagnosisResult
            val gemini = state.geminiAnalysisResult
            val plant = PlantEntity(
                id = state.editingPlantId ?: UUID.randomUUID().toString(),
                userId = null,
                customName = state.customName.trim(),
                commonName = state.commonName.trim(),
                scientificName = state.scientificName.trim(),
                description = state.description.trim(),
                photoUri = state.photoUri,
                photoUrl = null,
                locationType = state.growthLocation,
                lightExposure = state.sunlightExposure,
                leafStatus = state.leafStatus,
                soilHumidity = state.soilHumidity,
                currentWateringFrequency = state.currentWateringFrequency,
                identifiedByApi = state.identificationSuccess,
                identificationConfidence = state.confidence,
                isHealthyByDiseaseApi = disease?.isHealthy,
                diseaseName = disease?.diseaseName,
                diseaseProbability = disease?.probability,
                diseaseDescription = disease?.description,
                diseaseTreatmentRecommendation = disease?.treatmentRecommendation,
                recommendedWateringDays = carePlan.recommendedWateringDays,
                lightRecommendation = carePlan.lightRecommendation,
                fertilizationRecommendation = carePlan.fertilizationRecommendation,
                pruningRecommendation = carePlan.pruningRecommendation,
                diagnosis = carePlan.diagnosis,
                treatmentRecommendation = carePlan.treatmentRecommendation,
                geminiHealthStatus = gemini?.healthStatus,
                geminiDiagnosis = gemini?.diagnosis,
                geminiPossibleCauses = gemini?.possibleCauses?.joinToString("||"),
                geminiWateringRecommendation = gemini?.wateringRecommendation,
                geminiLightRecommendation = gemini?.lightRecommendation,
                geminiFertilizationRecommendation = gemini?.fertilizationRecommendation,
                geminiPruningRecommendation = gemini?.pruningRecommendation,
                geminiTreatmentRecommendation = gemini?.treatmentRecommendation,
                geminiWeeklyCarePlan = gemini?.weeklyCarePlan?.joinToString("||"),
                geminiRiskLevel = gemini?.riskLevel,
                geminiAnalyzedAt = if (gemini != null) now else null,
                tags = state.tags.joinToString(","),
                healthStatus = carePlan.urgency.storageValue,
                nextWateringAt = now + carePlan.recommendedWateringDays.daysInMillis(),
                nextReviewAt = now + carePlan.nextReviewDays.daysInMillis(),
                createdAt = state.createdAt ?: now,
                updatedAt = now
            )
            runCatching {
                if (state.isEditMode) {
                    withContext(Dispatchers.IO) { repository.updatePlant(plant) }
                } else {
                    withContext(Dispatchers.IO) { repository.savePlant(plant) }
                }
                Log.d(TAG, "[FLOW][SAVE_PLANT] Guardado en Room id=${plant.id}, editMode=${state.isEditMode}")
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaving = false,
                        successMessage = if (state.isEditMode) {
                            "Planta actualizada"
                        } else {
                            "Planta guardada correctamente"
                        },
                        saveSuccess = true,
                        savedSuccessfully = true
                    )
                }
            }.onFailure { error ->
                Log.e(TAG, "[FLOW][SAVE_PLANT] Error guardando planta: ${error.message}", error)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaving = false,
                        errorMessage = "No pudimos guardar la planta. Inténtalo nuevamente."
                    )
                }
            }
        }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(saveSuccess = false, savedSuccessfully = false) }
    }

    private fun String.toBase64(): String {
        val uri = Uri.parse(this)
        val bytes = contentResolver
            .openInputStream(uri)
            ?.use { it.readBytes() }
            ?: throw IllegalArgumentException("No se pudo leer la imagen")

        if (bytes.isEmpty()) {
            throw IllegalArgumentException("La imagen está vacía")
        }

        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun PlantFormUiState.toCondition(): PlantCondition {
        return PlantCondition(
            commonName = commonName,
            scientificName = scientificName,
            locationType = growthLocation,
            lightExposure = sunlightExposure,
            leafStatus = leafStatus,
            soilHumidity = soilHumidity,
            currentWateringFrequency = currentWateringFrequency,
            apiDescription = description,
            diseaseDiagnosis = diseaseDiagnosisResult
        )
    }

    private suspend fun analyzeWithGemini(imageBase64: String) {
        val state = _uiState.value
        _uiState.update {
            it.copy(
                isGeminiAnalyzing = true,
                geminiAnalysisMessage = null,
                geminiAnalysisErrorMessage = null
            )
        }
        Log.d(TAG, "[FLOW][GEMINI] Llamando Gemini para plan inteligente")
        withTimeoutOrNull(8_000) {
            geminiAnalysisUseCase(state.toGeminiInput(imageBase64))
        }?.onSuccess { analysis ->
            Log.d(TAG, "[FLOW][GEMINI] Gemini respondió, riskLevel=${analysis.riskLevel}")
            val mergedPlan = recommendationEngine.applyGeminiAnalysis(
                state.carePlan ?: recommendationEngine.generateCarePlan(state.toCondition()),
                analysis
            )
            _uiState.update {
                it.copy(
                    geminiAnalysisResult = analysis,
                    isGeminiAnalyzing = false,
                    geminiAnalysisMessage = "Recomendación lista",
                    geminiAnalysisErrorMessage = null,
                    carePlan = mergedPlan,
                    carePlanGenerated = true
                )
            }
        }?.onFailure { error ->
            Log.w(TAG, "[FLOW][GEMINI] Gemini falló, se mantiene fallback local: ${error.message}")
            val fallbackPlan = recommendationEngine.generateCarePlan(_uiState.value.toCondition())
            val fallbackMessage = "Generamos un plan básico para tu planta."
            _uiState.update {
                it.copy(
                    isGeminiAnalyzing = false,
                    geminiAnalysisErrorMessage = fallbackMessage,
                    carePlan = it.carePlan ?: fallbackPlan,
                    carePlanGenerated = true
                )
            }
        } ?: run {
            Log.w(TAG, "[FLOW][GEMINI] Timeout, se mantiene fallback local")
            val fallbackPlan = recommendationEngine.generateCarePlan(_uiState.value.toCondition())
            _uiState.update {
                it.copy(
                    isGeminiAnalyzing = false,
                    geminiAnalysisErrorMessage = "Generamos un plan básico para tu planta.",
                    carePlan = it.carePlan ?: fallbackPlan,
                    carePlanGenerated = true
                )
            }
        }
    }

    private fun PlantFormUiState.toGeminiInput(imageBase64: String): GeminiPlantAnalysisInput {
        return GeminiPlantAnalysisInput(
            imageBase64 = imageBase64,
            commonName = commonName,
            scientificName = scientificName,
            plantType = null,
            identificationConfidence = confidence,
            diseaseDiagnosis = diseaseDiagnosisResult,
            locationType = growthLocation,
            lightExposure = sunlightExposure,
            leafStatus = leafStatus,
            soilHumidity = soilHumidity,
            currentWateringFrequency = currentWateringFrequency
        )
    }

    private fun PlantEntity.toGeminiAnalysisResult(): GeminiPlantAnalysisResult? {
        if (
            geminiDiagnosis.isNullOrBlank() &&
            geminiHealthStatus.isNullOrBlank() &&
            geminiWeeklyCarePlan.isNullOrBlank()
        ) {
            return null
        }

        return GeminiPlantAnalysisResult(
            healthStatus = geminiHealthStatus.orEmpty(),
            diagnosis = geminiDiagnosis.orEmpty(),
            possibleCauses = geminiPossibleCauses.toListFromStorage(),
            wateringRecommendation = geminiWateringRecommendation.orEmpty(),
            lightRecommendation = geminiLightRecommendation.orEmpty(),
            fertilizationRecommendation = geminiFertilizationRecommendation.orEmpty(),
            pruningRecommendation = geminiPruningRecommendation.orEmpty(),
            treatmentRecommendation = geminiTreatmentRecommendation.orEmpty(),
            monthlyPlan = geminiWeeklyCarePlan.toListFromStorage()
        )
    }

    private fun String?.toListFromStorage(): List<String> {
        return orEmpty().split("||").map { it.trim() }.filter { it.isNotBlank() }
    }

    private fun Int.daysInMillis(): Long = this * 24L * 60L * 60L * 1000L

    private suspend fun runImageAnalysis(imageBase64: String) {
        _uiState.update {
            it.copy(
                isAnalyzing = true,
                isIdentifying = true,
                isDiseaseAnalyzing = true,
                identificationErrorMessage = null,
                diseaseAnalysisErrorMessage = null,
                geminiAnalysisErrorMessage = null
            )
        }

        val (identificationResponse, diseaseResponse) = coroutineScope {
            val identificationDeferred = async(Dispatchers.IO) {
                Log.d(TAG, "[FLOW][PLANT_ID] Llamando Plant.id identification")
                repository.identifyPlantFromImage(imageBase64)
            }
            val diseaseDeferred = async(Dispatchers.IO) {
                Log.d(TAG, "[FLOW][DISEASE] Llamando análisis de salud/enfermedad")
                repository.diagnosePlantDisease(imageBase64, null)
            }
            identificationDeferred.await() to diseaseDeferred.await()
        }

        identificationResponse.onSuccess { result ->
            Log.d(TAG, "[FLOW][PLANT_ID] Respuesta OK, commonName=${result.commonName}, confidence=${result.confidence}")
            applyIdentificationResult(result)
        }.onFailure { error ->
            Log.w(TAG, "[FLOW][PLANT_ID] Falló identificación, registro manual permitido: ${error.message}")
            _uiState.update {
                it.copy(
                    isIdentifying = false,
                    identificationErrorMessage = when (error) {
                        is IllegalArgumentException -> "Imagen inválida."
                        is IOException,
                        is PlantIdApiException -> "No pudimos identificar la planta, puedes continuar manualmente."
                        else -> "No pudimos identificar la planta, puedes continuar manualmente."
                    }
                )
            }
        }

        diseaseResponse.onSuccess { disease ->
            Log.d(TAG, "[FLOW][DISEASE] Respuesta OK, healthy=${disease.isHealthy}, disease=${disease.diseaseName}")
            _uiState.update {
                it.copy(
                    diseaseDiagnosisResult = disease,
                    isDiseaseAnalyzing = false,
                    diseaseAnalysisMessage = if (disease.isHealthy == false) {
                        "Revisa esta planta pronto"
                    } else {
                        "La planta parece saludable."
                    },
                    diseaseAnalysisErrorMessage = null,
                    carePlanGenerated = false
                )
            }
        }.onFailure { error ->
            Log.w(TAG, "[FLOW][DISEASE] Falló análisis de enfermedad, continúa flujo básico: ${error.message}")
            _uiState.update {
                it.copy(
                    isDiseaseAnalyzing = false,
                    diseaseAnalysisErrorMessage = "No pudimos revisar la salud, pero generamos un plan básico."
                )
            }
        }

        analyzeWithGemini(imageBase64)
        _uiState.update { it.copy(isAnalyzing = false) }
    }

    private companion object {
        private const val TAG = "PlantFormFlow"
    }
}
