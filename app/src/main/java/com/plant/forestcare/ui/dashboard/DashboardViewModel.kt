package com.plant.forestcare.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plant.forestcare.data.PlantRepository
import com.plant.forestcare.data.local.PlantEntity
import com.plant.forestcare.domain.care.CareUrgency
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: PlantRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = repository.getAllPlants()
        .map { plants ->
            val now = System.currentTimeMillis()
            val pendingCare = plants.count { it.nextWateringAt <= now }
            val healthy = plants.count { it.healthStatus == CareUrgency.Healthy.storageValue }
            val attention = plants.count { it.healthStatus == CareUrgency.NeedsAttention.storageValue }
            val urgent = plants.count { it.healthStatus == CareUrgency.Urgent.storageValue }
            val diseaseAlerts = plants.count { !it.diseaseName.isNullOrBlank() }
            val intelligentAlerts = plants
                .filter { it.geminiRiskLevel == "alto" || it.geminiRiskLevel == "medio" }
                .map {
                    "${it.customName.ifBlank { it.commonName }}: ${it.geminiDiagnosis ?: it.diagnosis}"
                }
                .take(3)
            val dailyRecommendations = plants
                .mapNotNull {
                    it.geminiTreatmentRecommendation
                        ?: it.geminiWateringRecommendation
                        ?: it.treatmentRecommendation.takeIf { recommendation -> recommendation.isNotBlank() }
                }
                .distinct()
                .take(3)
            DashboardUiState(
                isLoading = false,
                totalPlants = plants.size,
                pendingCareCount = pendingCare,
                healthyPlantsCount = healthy,
                attentionPlantsCount = attention,
                urgentPlantsCount = urgent,
                diseaseAlertsCount = diseaseAlerts,
                statusTitle = if (urgent > 0) "Revisa tus plantas hoy" else "Tu jardín va bien",
                statusMessage = if (urgent > 0) {
                    "Hay plantas que necesitan atención. Empieza por las marcadas como urgentes."
                } else {
                    "Mantén los riegos y revisiones al día para que sigan creciendo sanas."
                },
                generalHealthPercentage = if (plants.isEmpty()) 0 else ((healthy.toFloat() / plants.size) * 100).toInt(),
                nextWateringText = plants.minByOrNull { it.nextWateringAt }
                    ?.nextWateringAt
                    ?.toRelativeWateringText()
                    ?: "Sin riegos",
                nextReviewText = plants.minByOrNull { it.nextReviewAt }
                    ?.nextReviewAt
                    ?.toRelativeWateringText()
                    ?: "Sin revisiones",
                intelligentAlerts = intelligentAlerts,
                dailyRecommendations = dailyRecommendations,
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
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )
}

private fun PlantEntity.toPreviewUi(): PlantPreviewUi {
    return PlantPreviewUi(
        id = id,
        category = locationType.uppercase(),
        name = customName.ifBlank { commonName },
        subtitle = (geminiDiagnosis ?: diagnosis).ifBlank { commonName.ifBlank { scientificName.ifBlank { "Planta guardada" } } },
        health = CareUrgency.fromStorage(healthStatus).displayName,
        watering = nextWateringAt.toRelativeWateringText(),
        urgentWatering = nextWateringAt <= System.currentTimeMillis(),
        imageType = when {
            commonName.contains("pothos", ignoreCase = true) -> PlantImageType.Pothos
            commonName.contains("sansevieria", ignoreCase = true) -> PlantImageType.Sansevieria
            else -> PlantImageType.Monstera
        }
    )
}

private fun Long.toRelativeWateringText(): String {
    val diff = this - System.currentTimeMillis()
    if (diff <= 0L) return "Hoy"
    val days = TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(1)
    return if (days == 1L) "Mañana" else "En $days días"
}
