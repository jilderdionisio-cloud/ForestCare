package com.plant.forestcare.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.plant.forestcare.data.PlantRepository
import com.plant.forestcare.data.local.PlantEntity
import com.plant.forestcare.domain.care.CareUrgency
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlantDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlantRepository.getInstance(application)
    private var loadJob: Job? = null
    private var currentEntity: PlantEntity? = null

    private val _uiState = MutableStateFlow(PlantDetailUiState())
    val uiState: StateFlow<PlantDetailUiState> = _uiState.asStateFlow()

    fun loadPlant(plantId: String) {
        if (loadJob?.isActive == true && currentEntity?.id == plantId) return

        loadJob?.cancel()
        _uiState.value = PlantDetailUiState(isLoading = true)
        loadJob = viewModelScope.launch {
            repository.getPlantById(plantId).collect { plant ->
                currentEntity = plant
                _uiState.value = if (plant == null) {
                    PlantDetailUiState(
                        isLoading = false,
                        errorMessage = "No encontramos esta planta"
                    )
                } else {
                    PlantDetailUiState(
                        isLoading = false,
                        plant = plant.toDetailUi()
                    )
                }
            }
        }
    }

    fun deletePlant() {
        val plant = currentEntity ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, errorMessage = null) }
            runCatching {
                repository.deletePlant(plant)
            }.onSuccess {
                _uiState.update { it.copy(isDeleting = false, deletedSuccessfully = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = error.message ?: "No se pudo eliminar la planta"
                    )
                }
            }
        }
    }

    fun markWateringDone() {
        val plant = currentEntity ?: return
        viewModelScope.launch {
            runCatching {
                repository.markWateringDone(plant)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "No se pudo registrar el cuidado")
                }
            }
        }
    }

    fun onDeleteNavigationHandled() {
        _uiState.update { it.copy(deletedSuccessfully = false) }
    }
}

private fun PlantEntity.toDetailUi(): PlantDetailUi {
    return PlantDetailUi(
        id = id,
        name = customName.ifBlank { commonName.ifBlank { "Planta sin nombre" } },
        commonName = commonName,
        scientificName = scientificName,
        description = description,
        location = locationType,
        sunlightExposure = lightExposure,
        leafStatus = leafStatus,
        soilHumidity = soilHumidity,
        currentWateringFrequency = currentWateringFrequency,
        recommendedWateringDays = recommendedWateringDays,
        diagnosis = diagnosis,
        diseaseName = diseaseName,
        diseaseProbability = diseaseProbability,
        diseaseDescription = diseaseDescription,
        diseaseTreatmentRecommendation = diseaseTreatmentRecommendation,
        geminiHealthStatus = geminiHealthStatus,
        geminiDiagnosis = geminiDiagnosis,
        geminiPossibleCauses = geminiPossibleCauses.toListFromStorage(),
        geminiWateringRecommendation = geminiWateringRecommendation,
        geminiLightRecommendation = geminiLightRecommendation,
        geminiFertilizationRecommendation = geminiFertilizationRecommendation,
        geminiPruningRecommendation = geminiPruningRecommendation,
        geminiTreatmentRecommendation = geminiTreatmentRecommendation,
        geminiWeeklyCarePlan = geminiWeeklyCarePlan.toListFromStorage(),
        geminiRiskLevel = geminiRiskLevel,
        treatmentRecommendation = treatmentRecommendation,
        lightRecommendation = lightRecommendation,
        fertilizationRecommendation = fertilizationRecommendation,
        pruningRecommendation = pruningRecommendation,
        nextWateringText = nextWateringAt.toRelativeWateringText(),
        nextReviewText = nextReviewAt.toRelativeWateringText(),
        tags = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        photoUri = photoUri,
        healthStatus = CareUrgency.fromStorage(healthStatus).displayName
    )
}

private fun String?.toListFromStorage(): List<String> {
    return orEmpty().split("||").map { it.trim() }.filter { it.isNotBlank() }
}

private fun Long.toRelativeWateringText(): String {
    val diff = this - System.currentTimeMillis()
    if (diff <= 0L) return "Hoy"
    val days = TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(1)
    return if (days == 1L) "Mañana" else "En $days días"
}
