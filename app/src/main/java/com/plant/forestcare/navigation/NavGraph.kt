package com.plant.forestcare.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.plant.forestcare.ui.dashboard.DashboardRoute
import com.plant.forestcare.ui.form.PlantFormRoute

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardRoute(navController = navController)
        }
        composable(Screen.PlantList.route) {
            PlaceholderScreen("Lista de plantas")
        }
        composable(Screen.PlantForm.route) {
            PlantFormRoute(navController = navController)
        }
        composable(Screen.PlantDetail.route) {
            PlaceholderScreen("Detalle de planta")
        }
        composable(Screen.Reminders.route) {
            PlaceholderScreen("Recordatorios")
        }
        composable(Screen.Profile.route) {
            PlaceholderScreen("Perfil")
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
