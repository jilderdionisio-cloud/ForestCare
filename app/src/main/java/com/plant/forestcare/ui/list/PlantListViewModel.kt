package com.plant.forestcare.ui.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.plant.forestcare.data.PlantRepository
import com.plant.forestcare.data.local.PlantEntity
import com.plant.forestcare.domain.care.CareUrgency
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class PlantListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlantRepository.getInstance(application)
    private val retryTrigger = MutableStateFlow(0)
    private val searchQuery = MutableStateFlow("")
    private val selectedFilter = MutableStateFlow(PlantListFilter.All)

    val uiState: StateFlow<PlantListUiState> = retryTrigger
        .flatMapLatest {
            repository.observePlants()
                .onStart { Log.d(TAG, "[FLOW][PLANT_LIST] Observando plantas desde Room") }
                .combine(searchQuery) { plants, query -> plants to query }
                .combine(selectedFilter) { (plants, query), filter -> Triple(plants, query, filter) }
                .map { (plants, query, filter) ->
                    Log.d(TAG, "[FLOW][PLANT_LIST] Room emitió ${plants.size} plantas")
                    val filteredPlants = plants
                        .map { it.toListItemUi() }
                        .filter { it.matches(query, filter) }
                    Log.d(TAG, "[FLOW][PLANT_LIST] Lista visible=${filteredPlants.size}, filtro=${filter.name}, busquedaActiva=${query.isNotBlank()}")
                    PlantListUiState(
                        isLoading = false,
                        plants = filteredPlants,
                        searchQuery = query,
                        selectedFilter = filter
                    )
                }
                .catch { error ->
                    Log.e(TAG, "[FLOW][PLANT_LIST] Error cargando plantas desde Room: ${error.message}", error)
                    emit(
                        PlantListUiState(
                            isLoading = false,
                            searchQuery = searchQuery.value,
                            selectedFilter = selectedFilter.value,
                            errorMessage = error.message ?: "No se pudieron cargar tus plantas"
                        )
                    )
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlantListUiState()
        )

    fun retry() {
        retryTrigger.update { it + 1 }
    }

    fun onSearchQueryChange(value: String) {
        searchQuery.value = value
    }

    fun onFilterSelected(filter: PlantListFilter) {
        selectedFilter.value = filter
    }

    private companion object {
        private const val TAG = "PlantListFlow"
    }
}

private fun PlantListItemUi.matches(query: String, filter: PlantListFilter): Boolean {
    val matchesQuery = query.isBlank() ||
        name.contains(query, ignoreCase = true) ||
        scientificName.contains(query, ignoreCase = true)
    val matchesFilter = when (filter) {
        PlantListFilter.All -> true
        PlantListFilter.Healthy -> healthStatus.equals(CareUrgency.Healthy.displayName, ignoreCase = true)
        PlantListFilter.Attention -> healthStatus.equals(CareUrgency.NeedsAttention.displayName, ignoreCase = true)
        PlantListFilter.Urgent -> healthStatus.equals(CareUrgency.Urgent.displayName, ignoreCase = true)
    }
    return matchesQuery && matchesFilter
}

private fun PlantEntity.toListItemUi(): PlantListItemUi {
    return PlantListItemUi(
        id = id,
        name = customName.ifBlank { commonName.ifBlank { "Planta sin nombre" } },
        scientificName = scientificName,
        location = locationType,
        healthStatus = CareUrgency.fromStorage(healthStatus).displayName,
        hasDisease = !diseaseName.isNullOrBlank(),
        nextCareText = nextWateringAt.toRelativeWateringText(),
        nextReviewText = nextReviewAt.toRelativeWateringText(),
        diagnosis = geminiDiagnosis?.takeIf { it.isNotBlank() } ?: diagnosis,
        riskLevel = geminiRiskLevel,
        sunlightExposure = lightExposure,
        photoUri = photoUri
    )
}

private fun Long.toRelativeWateringText(): String {
    val diff = this - System.currentTimeMillis()
    if (diff <= 0L) return "Hoy"
    val days = TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(1)
    return if (days == 1L) "Mañana" else "En $days días"
}
