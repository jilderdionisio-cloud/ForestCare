package com.plant.forestcare.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Dashboard : Screen("dashboard")
    data object PlantList : Screen("plants_list")
    data object PlantCamera : Screen("plant_camera")
    data object PlantForm : Screen("plant_form")
    data object PlantFormEdit : Screen("plant_form/{plantId}") {
        fun createRoute(plantId: String): String = "plant_form/$plantId"
    }
    data object PlantDetail : Screen("plant_detail/{plantId}") {
        fun createRoute(plantId: String): String = "plant_detail/$plantId"
    }
    data object Reminders : Screen("reminders")
    data object Profile : Screen("profile")
    data object ApiDiagnostics : Screen("api_diagnostics")
}
