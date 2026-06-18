package com.plant.forestcare.ui.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        DashboardUiState(
            plants = listOf(
                PlantPreviewUi(
                    id = 1,
                    category = "INTERIOR",
                    name = "Monstera Deliciosa",
                    subtitle = "La gigante verde",
                    health = "Salud Excelente",
                    watering = "En 2 días",
                    imageType = PlantImageType.Monstera
                ),
                PlantPreviewUi(
                    id = 2,
                    category = "COLGANTE",
                    name = "Pothos Marble",
                    subtitle = "Cascada de Mármol",
                    health = "Salud Estable",
                    watering = "Agua Toca hoy!",
                    urgentWatering = true,
                    imageType = PlantImageType.Pothos
                ),
                PlantPreviewUi(
                    id = 3,
                    category = "RESISTENTE",
                    name = "Sansevieria",
                    subtitle = "Lengua de Suegra",
                    health = "Salud Vigorosa",
                    watering = "Agua En 12 días",
                    imageType = PlantImageType.Sansevieria
                )
            )
        )
    )

    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
}
