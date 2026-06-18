package com.plant.forestcare.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            PlaceholderScreen(
                title = "Plantas",
                message = "Esta sección se implementará próximamente",
                activeRoute = Screen.PlantList.route,
                onNavigate = { route -> navController.navigateTopLevel(route) }
            )
        }
        composable(Screen.PlantForm.route) {
            PlantFormRoute(navController = navController)
        }
        composable(Screen.PlantDetail.route) {
            PlaceholderScreen(
                title = "Detalle de planta",
                message = "Esta sección se implementará próximamente",
                activeRoute = Screen.PlantList.route,
                onNavigate = { route -> navController.navigateTopLevel(route) }
            )
        }
        composable(Screen.Reminders.route) {
            PlaceholderScreen(
                title = "Recordatorios",
                message = "Esta sección se implementará próximamente",
                activeRoute = Screen.Reminders.route,
                onNavigate = { route -> navController.navigateTopLevel(route) }
            )
        }
        composable(Screen.Profile.route) {
            PlaceholderScreen(
                title = "Perfil",
                message = "Esta sección se implementará próximamente",
                activeRoute = Screen.Profile.route,
                onNavigate = { route -> navController.navigateTopLevel(route) }
            )
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    message: String,
    activeRoute: String,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFFAFCF7),
        bottomBar = {
            PlaceholderBottomBar(activeRoute = activeRoute, onNavigate = onNavigate)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF102116)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6E7A70),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun PlaceholderBottomBar(
    activeRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("Inicio", Screen.Dashboard.route, Icons.Rounded.Home),
        BottomNavItem("Plantas", Screen.PlantList.route, Icons.Rounded.Spa),
        BottomNavItem("Dashboard", Screen.Dashboard.route, Icons.Rounded.Dashboard),
        BottomNavItem("Recordatorios", Screen.Reminders.route, Icons.Rounded.Notifications),
        BottomNavItem("Perfil", Screen.Profile.route, Icons.Rounded.Person)
    )

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 12.dp
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier.height(74.dp)
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = item.route == activeRoute && (item.label != "Inicio" || activeRoute == Screen.Dashboard.route),
                    onClick = { onNavigate(item.route) },
                    icon = { androidx.compose.material3.Icon(item.icon, contentDescription = item.label) },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 9.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4CAF50),
                        selectedTextColor = Color(0xFF4CAF50),
                        unselectedIconColor = Color(0xFF8E8E8E),
                        unselectedTextColor = Color(0xFF8E8E8E),
                        indicatorColor = Color.Transparent
                    )
                )
            }
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

private data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)
