package com.plant.forestcare.ui.form

import com.plant.forestcare.domain.model.PlantSpecies

data class PlantFormUiState(
    val customName: String = "",
    val commonName: String = "",
    val scientificName: String = "",
    val description: String = "",
    val growthLocation: String = "Interior",
    val sunlightExposure: String = "Media",
    val photoUri: String? = null,
    val tags: List<String> = listOf("Apta para mascotas", "Purificadora de aire"),
    val searchQuery: String = "",
    val apiResults: List<PlantSpecies> = emptyList(),
    val isSearching: Boolean = false,
    val apiErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val savedSuccessfully: Boolean = false
)
