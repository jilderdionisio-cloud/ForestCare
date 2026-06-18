package com.plant.forestcare.ui.form

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlantFormViewModel : ViewModel() {
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

    fun onAddTag(tag: String = "New Tag") {
        _uiState.update { state ->
            if (state.tags.contains(tag)) state else state.copy(tags = state.tags + tag)
        }
    }

    fun onRemoveTag(tag: String) {
        _uiState.update { it.copy(tags = it.tags.filterNot { currentTag -> currentTag == tag }) }
    }

    fun savePlant() {
        val state = _uiState.value
        if (state.customName.isBlank() && state.commonName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Agrega al menos un nombre para la planta.") }
            return
        }

        // TODO: Persistir con Repository cuando la capa de datos esté lista.
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                successMessage = "Planta guardada"
            )
        }
    }

    fun onMessageShown() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
