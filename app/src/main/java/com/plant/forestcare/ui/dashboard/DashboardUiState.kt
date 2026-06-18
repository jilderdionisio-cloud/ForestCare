package com.plant.forestcare.ui.dashboard

data class DashboardUiState(
    val userName: String = "Mateo",
    val greeting: String = "Good morning, Grower",
    val temperature: String = "24°C",
    val statusTitle: String = "TU JARDÍN ESTÁ PROSPERANDO",
    val statusMessage: String = "Es un gran día para hidratar tu Monstera. La humedad ambiente es ideal para el crecimiento hoy.",
    val totalPlants: Int = 12,
    val nextWatering: String = "Hoy",
    val pendingTasks: Int = 3,
    val overallHealth: Int = 92,
    val plants: List<PlantPreviewUi> = emptyList()
)

data class PlantPreviewUi(
    val id: Int,
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
