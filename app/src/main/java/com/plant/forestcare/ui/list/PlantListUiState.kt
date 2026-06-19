package com.plant.forestcare.ui.list

data class PlantListUiState(
    val isLoading: Boolean = true,
    val plants: List<PlantListItemUi> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: PlantListFilter = PlantListFilter.All,
    val errorMessage: String? = null
) {
    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && plants.isEmpty()
}

enum class PlantListFilter(val label: String) {
    All("Todas"),
    Healthy("Saludables"),
    Attention("Revisar pronto"),
    Urgent("Urgentes")
}

data class PlantListItemUi(
    val id: String,
    val name: String,
    val scientificName: String,
    val location: String,
    val healthStatus: String,
    val hasDisease: Boolean,
    val nextCareText: String,
    val nextReviewText: String,
    val diagnosis: String,
    val riskLevel: String?,
    val sunlightExposure: String,
    val photoUri: String?
)
