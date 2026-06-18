package com.plant.forestcare.ui.dashboard

data class DashboardUiState(
    val userName: String = "Mateo",
    val greeting: String = "Buenos días, jardinero",
    val temperature: String = "24°C",
    val statusTitle: String = "TU JARDÍN ESTÁ PROSPERANDO",
    val statusMessage: String = "Es un gran día para hidratar tu Monstera. La humedad ambiente es ideal para el crecimiento hoy.",
    val isLoading: Boolean = true,
    val totalPlants: Int = 0,
    val pendingCareCount: Int = 0,
    val generalHealthPercentage: Int = 0,
    val nextWateringText: String = "Sin riegos",
    val errorMessage: String? = null,
    val plants: List<PlantPreviewUi> = emptyList()
)

data class PlantPreviewUi(
    val id: String,
    val category: String,
    val name: String,
    val subtitle: String,
    val health: String,
    val watering: String,
    val urgentWatering: Boolean = false,
    val imageType: PlantImageType
)

enum class PlantImageType {
    Monstera,
    Pothos,
    Sansevieria
}
