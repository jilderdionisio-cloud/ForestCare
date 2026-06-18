package com.plant.forestcare.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object PlantList : Screen("plants_list")
    data object PlantForm : Screen("plant_form")
    data object PlantDetail : Screen("plant_detail/{plantId}") {
        fun createRoute(plantId: String): String = "plant_detail/$plantId"
    }
    data object Reminders : Screen("reminders")
    data object Profile : Screen("profile")
}
