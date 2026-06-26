package com.plant.forestcare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.plant.forestcare.ui.auth.LoginRoute
import com.plant.forestcare.ui.auth.RegisterRoute
import com.plant.forestcare.ui.dashboard.DashboardRoute
import com.plant.forestcare.ui.diagnostics.ApiDiagnosticsRoute
import com.plant.forestcare.ui.detail.PlantDetailRoute
import com.plant.forestcare.ui.form.PlantCameraRoute
import com.plant.forestcare.ui.form.PlantFormRoute
import com.plant.forestcare.ui.home.HomeRoute
import com.plant.forestcare.ui.list.PlantListRoute
import com.plant.forestcare.ui.onboarding.OnboardingRoute
import com.plant.forestcare.ui.onboarding.SplashRoute
import com.plant.forestcare.ui.profile.ProfileRoute
import com.plant.forestcare.ui.reminders.RemindersRoute

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Dashboard.route,
    onOnboardingFinished: () -> Unit = {},
    onAuthFinished: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashRoute(
                onFinished = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.Onboarding.route) {
            OnboardingRoute(
                onFinished = {
                    onOnboardingFinished()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginRoute(
                onLoginSuccess = {
                    onAuthFinished()
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterRoute(
                onRegisterSuccess = {
                    onAuthFinished()
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onLoginClick = {
                    navController.popBackStack(Screen.Login.route, inclusive = false)
                }
            )
        }
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
