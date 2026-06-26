package com.plant.forestcare.ui.diagnostics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plant.forestcare.data.diagnostics.ApiDiagnosticResult
import com.plant.forestcare.data.diagnostics.ApiDiagnosticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiDiagnosticsViewModel @Inject constructor(
    private val repository: ApiDiagnosticsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ApiDiagnosticsUiState())
    val uiState: StateFlow<ApiDiagnosticsUiState> = _uiState.asStateFlow()

    fun onImageBase64Change(value: String) {
        _uiState.update { it.copy(imageBase64 = value, errorMessage = null) }
    }

    fun verifyAllApis() {
        val imageBase64 = _uiState.value.imageBase64.trim()
        if (imageBase64.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Pega una imagen Base64 válida para ejecutar la prueba.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                repository.verifyAllApis(imageBase64)
            }.onSuccess { report ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        plantIdentification = report.plantIdentification,
                        diseaseDetection = report.diseaseDetection,
                        gemini = report.gemini
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo ejecutar el diagnóstico."
                    )
                }
            }
        }
    }
}

data class ApiDiagnosticsUiState(
    val imageBase64: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val plantIdentification: ApiDiagnosticResult? = null,
    val diseaseDetection: ApiDiagnosticResult? = null,
    val gemini: ApiDiagnosticResult? = null
)
