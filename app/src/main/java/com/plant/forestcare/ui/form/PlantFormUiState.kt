package com.plant.forestcare.ui.form

data class PlantFormUiState(
    val customName: String = "",
    val commonName: String = "",
    val scientificName: String = "",
    val description: String = "",
    val growthLocation: String = "Indoor",
    val sunlightExposure: String = "Medium",
    val tags: List<String> = listOf("Pet Friendly", "Air Purifier"),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
