package com.plant.forestcare.ui.form

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plant.forestcare.data.PlantRepository
import com.plant.forestcare.data.local.PlantEntity
import java.util.UUID
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlantFormViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlantRepository.getInstance(application)

    private val _uiState = MutableStateFlow(PlantFormUiState())
    val uiState: StateFlow<PlantFormUiState> = _uiState.asStateFlow()

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
        _uiState.update { it.copy(growthLocation = value) }
    }

    fun onSunlightExposureChange(value: String) {
        _uiState.update { it.copy(sunlightExposure = value) }
    }

    fun onAddTag(tag: String = "Nueva etiqueta") {
        _uiState.update { state ->
            if (state.tags.contains(tag)) state else state.copy(tags = state.tags + tag)
        }
    }

    fun onRemoveTag(tag: String) {
        _uiState.update { it.copy(tags = it.tags.filterNot { currentTag -> currentTag == tag }) }
    }

    fun savePlant() {
        val state = _uiState.value
        if (
            state.customName.isBlank() ||
            state.commonName.isBlank() ||
            state.growthLocation.isBlank() ||
            state.sunlightExposure.isBlank()
        ) {
            _uiState.update { it.copy(errorMessage = "Completa los campos obligatorios") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            val now = System.currentTimeMillis()
            val plant = PlantEntity(
                id = UUID.randomUUID().toString(),
                customName = state.customName.trim(),
                commonName = state.commonName.trim(),
                scientificName = state.scientificName.trim(),
                description = state.description.trim(),
                location = state.growthLocation,
                sunlightExposure = state.sunlightExposure,
                tags = state.tags.joinToString(","),
                photoUri = null,
                healthStatus = "Saludable",
                nextWateringText = "En 2 días",
                createdAt = now,
                updatedAt = now
            )
            runCatching {
                repository.savePlant(plant)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Planta guardada correctamente",
                        savedSuccessfully = true
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo guardar la planta"
                    )
                }
            }
        }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(savedSuccessfully = false) }
    }
}
