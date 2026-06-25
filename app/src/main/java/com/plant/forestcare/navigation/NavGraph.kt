package com.plant.forestcare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.plant.forestcare.ui.dashboard.DashboardRoute
import com.plant.forestcare.ui.diagnostics.ApiDiagnosticsRoute
import com.plant.forestcare.ui.detail.PlantDetailRoute
import com.plant.forestcare.ui.form.PlantCameraRoute
import com.plant.forestcare.ui.form.PlantFormRoute
import com.plant.forestcare.ui.home.HomeRoute
import com.plant.forestcare.ui.list.PlantListRoute
import com.plant.forestcare.ui.profile.ProfileRoute
import com.plant.forestcare.ui.reminders.RemindersRoute

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
        composable(Screen.Home.route) {
            HomeRoute(navController = navController)
        }
        composable(Screen.Dashboard.route) {
            DashboardRoute(navController = navController)
        }
        composable(Screen.PlantList.route) {
            PlantListRoute(
                onPlantClick = { plantId ->
                    navController.navigate(Screen.PlantDetail.createRoute(plantId))
                },
                onAddPlantClick = {
                    navController.navigate(Screen.PlantCamera.route)
                },
                onNavigate = { route -> navController.navigateTopLevel(route) }
            )
        }
        composable(Screen.PlantForm.route) {
            PlantFormRoute(navController = navController)
        }
        composable(Screen.PlantCamera.route) {
            PlantCameraRoute(navController = navController)
        }
        composable(
            route = Screen.PlantFormEdit.route,
            arguments = listOf(navArgument("plantId") { type = NavType.StringType })
        ) { backStackEntry ->
            PlantFormRoute(
                navController = navController,
                plantId = backStackEntry.arguments?.getString("plantId")
            )
        }
        composable(
            route = Screen.PlantDetail.route,
            arguments = listOf(navArgument("plantId") { type = NavType.StringType })
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId").orEmpty()
            PlantDetailRoute(
                plantId = plantId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(Screen.PlantFormEdit.createRoute(id)) },
                onUpdatePhotoClick = { navController.navigate(Screen.PlantCamera.route) },
                onDeleted = {
                    navController.navigate(Screen.PlantList.route) {
                        popUpTo(Screen.PlantList.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onNavigate = { route -> navController.navigateTopLevel(route) }
            )
        }
        composable(Screen.Reminders.route) {
            RemindersRoute(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileRoute(navController = navController)
        }
        composable(Screen.ApiDiagnostics.route) {
            ApiDiagnosticsRoute()
        }
    }
}

private fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(Screen.Dashboard.route) {
            saveState = true
        }
    }
}
