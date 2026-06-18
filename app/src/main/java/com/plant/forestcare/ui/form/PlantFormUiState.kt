package com.plant.forestcare.ui.form

data class PlantFormUiState(
    val customName: String = "",
    val commonName: String = "",
    val scientificName: String = "",
    val description: String = "",
    val growthLocation: String = "Interior",
    val sunlightExposure: String = "Media",
    val tags: List<String> = listOf("Apta para mascotas", "Purificadora de aire"),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val savedSuccessfully: Boolean = false
)
