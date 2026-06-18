package com.plant.forestcare.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.plant.forestcare.data.PlantRepository
import com.plant.forestcare.data.local.PlantEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlantRepository.getInstance(application)

    val uiState: StateFlow<DashboardUiState> = repository.getAllPlants()
        .map { plants ->
            val pendingCare = plants.count { it.nextWateringText.equals("Hoy", ignoreCase = true) }
            DashboardUiState(
                isLoading = false,
                totalPlants = plants.size,
                pendingCareCount = pendingCare,
                generalHealthPercentage = if (plants.isEmpty()) 0 else 92,
                nextWateringText = plants.firstOrNull { it.nextWateringText.equals("Hoy", ignoreCase = true) }
                    ?.nextWateringText
                    ?: plants.firstOrNull()?.nextWateringText
                    ?: "Sin riegos",
                plants = plants.take(5).map { it.toPreviewUi() }
            )
        }
        .catch { error ->
            emit(
                DashboardUiState(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudieron cargar las plantas"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )
}

private fun PlantEntity.toPreviewUi(): PlantPreviewUi {
    return PlantPreviewUi(
        id = id,
        category = location.uppercase(),
        name = customName.ifBlank { commonName },
        subtitle = commonName.ifBlank { scientificName.ifBlank { "Planta guardada" } },
        health = healthStatus,
        watering = nextWateringText,
        urgentWatering = nextWateringText.equals("Hoy", ignoreCase = true),
        imageType = when {
            commonName.contains("pothos", ignoreCase = true) -> PlantImageType.Pothos
            commonName.contains("sansevieria", ignoreCase = true) -> PlantImageType.Sansevieria
            else -> PlantImageType.Monstera
        }
    )
}
