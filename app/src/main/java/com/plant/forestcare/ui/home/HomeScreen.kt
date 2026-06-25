package com.plant.forestcare.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.plant.forestcare.navigation.Screen
import com.plant.forestcare.ui.components.ForestCareBottomBar
import com.plant.forestcare.ui.dashboard.DashboardViewModel

private val PlantBackground = Color(0xFFFAFCF7)
private val PlantGreen = Color(0xFF4CAF50)
private val PlantGreenDark = Color(0xFF1F5B34)
private val TextPrimary = Color(0xFF102116)
private val TextMuted = Color(0xFF6E7A70)

@Composable
fun HomeRoute(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        totalPlants = uiState.totalPlants,
        healthyPlants = uiState.healthyPlantsCount,
        urgentPlants = uiState.urgentPlantsCount,
        nextWateringText = uiState.nextWateringText,
        onAddPlant = { navController.navigate(Screen.PlantForm.route) },
        onOpenPlants = { navController.navigateTopLevelHome(Screen.PlantList.route) },
        onOpenDashboard = { navController.navigateTopLevelHome(Screen.Dashboard.route) },
        onNavigate = { route -> navController.navigateTopLevelHome(route) }
    )
}

@Composable
private fun HomeScreen(
    totalPlants: Int,
    healthyPlants: Int,
    urgentPlants: Int,
    nextWateringText: String,
    onAddPlant: () -> Unit,
    onOpenPlants: () -> Unit,
    onOpenDashboard: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = PlantBackground,
        bottomBar = {
            ForestCareBottomBar(activeRoute = Screen.Home.route, onNavigate = onNavigate)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HomeHeader()
            }
            item {
                Surface(
                    color = PlantGreen,
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF43A047), Color(0xFF66BB6A))
                                )
                            )
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (totalPlants == 0) "Registra tu primera planta" else "Tu jardín está listo para revisar",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (totalPlants == 0) {
                                "Toma una foto, analiza su salud y recibe un plan de cuidado simple."
                            } else {
                                "Próximo riego: $nextWateringText. Cuidados urgentes: $urgentPlants."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.92f)
                        )
                        Button(
                            onClick = onAddPlant,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null, tint = PlantGreen)
                            Spacer(Modifier.size(8.dp))
                            Text("Agregar planta", color = PlantGreenDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    HomeMetricCard("Plantas", totalPlants.toString(), Modifier.weight(1f), onOpenPlants)
                    HomeMetricCard("Saludables", healthyPlants.toString(), Modifier.weight(1f), onOpenDashboard)
                }
            }
            item {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(22.dp),
                    shadowElevation = 6.dp,
                    modifier = Modifier.clickable(onClick = onOpenDashboard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE7F6E8)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.WaterDrop, contentDescription = null, tint = PlantGreen)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Resumen de cuidados", color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Abre el dashboard para ver alertas, salud y próximos cuidados.",
                                color = TextMuted,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2F3D8)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Spa, contentDescription = null, tint = PlantGreenDark)
        }
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text("ForestCare", color = TextPrimary, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Cuidado simple para tus plantas", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun HomeMetricCard(
    label: String,
    value: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 6.dp,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(value, color = TextPrimary, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, color = TextMuted, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun NavController.navigateTopLevelHome(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(Screen.Home.route) {
            saveState = true
        }
    }
}
