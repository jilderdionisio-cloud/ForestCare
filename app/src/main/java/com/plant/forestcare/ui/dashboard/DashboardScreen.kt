package com.plant.forestcare.ui.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.plant.forestcare.navigation.Screen
import com.plant.forestcare.ui.theme.ForestCareTheme

private val PlantBackground = Color(0xFFFAFCF7)
private val PlantGreen = Color(0xFF4CAF50)
private val PlantGreenDark = Color(0xFF1F5B34)
private val PlantGreenStrong = Color(0xFF00A651)
private val TextPrimary = Color(0xFF102116)
private val TextMuted = Color(0xFF6E7A70)
private val WarmBeige = Color(0xFFF3EAD9)
private val AlertRed = Color(0xFFE34D4D)
private val UrgentOrange = Color(0xFFE86F3A)

@Composable
fun DashboardRoute(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        uiState = uiState,
        onAddPlant = { navController.navigate(Screen.PlantForm.route) },
        onViewAllPlants = { navController.navigate(Screen.PlantList.route) },
        onPlantClick = { plantId -> navController.navigate(Screen.PlantDetail.createRoute(plantId)) },
        onNavigate = { route ->
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(Screen.Dashboard.route) {
                    saveState = true
                }
            }
        }
    )
}

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onAddPlant: () -> Unit,
    onViewAllPlants: () -> Unit,
    onPlantClick: (String) -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PlantBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPlant,
                containerColor = PlantGreenStrong,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(54.dp)
                    .offset(y = 10.dp)
                    .shadow(10.dp, CircleShape, ambientColor = PlantGreen.copy(alpha = 0.28f))
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Agregar planta")
            }
        },
        bottomBar = {
            DashboardBottomBar(
                activeRoute = Screen.Dashboard.route,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, top = 18.dp, end = 16.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DashboardHeader(uiState = uiState)
            }
            item {
                GardenStatusBanner(
                    title = uiState.statusTitle,
                    message = uiState.statusMessage
                )
            }
            item {
                SummaryStatGrid(uiState = uiState)
            }
            item {
                PlantsSectionHeader(onViewAllPlants = onViewAllPlants)
            }
            if (uiState.plants.isEmpty() && !uiState.isLoading) {
                item {
                    EmptyPlantsState(onAddPlant = onAddPlant)
                }
            } else {
                items(uiState.plants, key = { it.id }) { plant ->
                    PlantPreviewCard(
                        plant = plant,
                        onClick = { onPlantClick(plant.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader(uiState: DashboardUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFE2F3D8), Color(0xFFC9E6BB))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "M", color = PlantGreenDark, fontWeight = FontWeight.Black, fontSize = 18.sp)
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "¡Hola, ${uiState.userName}!",
                color = TextMuted,
                fontSize = 11.sp,
                lineHeight = 13.sp
            )
            Text(
                text = uiState.greeting,
                color = TextPrimary,
                fontSize = 20.sp,
                lineHeight = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }
        WeatherPill(temperature = uiState.temperature)
    }
}

@Composable
private fun WeatherPill(temperature: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.WbSunny,
                contentDescription = null,
                tint = Color(0xFFF7B733),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = temperature,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun GardenStatusBanner(title: String, message: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF43A047), Color(0xFF66BB6A))
                    )
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Eco,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.4.sp
                )
            }
        }
        Text(
            text = message,
            color = TextMuted,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            modifier = Modifier.padding(horizontal = 2.dp)
        )
    }
}

@Composable
private fun SummaryStatGrid(uiState: DashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryStatCard(
                icon = Icons.Rounded.LocalFlorist,
                value = uiState.totalPlants.toString(),
                label = "Total plantas",
                modifier = Modifier.weight(1f)
            )
            SummaryStatCard(
                icon = Icons.Rounded.WaterDrop,
                value = uiState.nextWateringText,
                label = "Próximo riego",
                highlighted = true,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryStatCard(
                icon = Icons.Rounded.ErrorOutline,
                value = uiState.pendingCareCount.toString(),
                label = "Pendientes",
                accentColor = AlertRed,
                showAlertCurve = true,
                modifier = Modifier.weight(1f)
            )
            SummaryStatCard(
                icon = Icons.Rounded.HealthAndSafety,
                value = "${uiState.generalHealthPercentage}%",
                label = "Salud general",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun EmptyPlantsState(onAddPlant: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Spa,
            contentDescription = null,
            tint = PlantGreen,
            modifier = Modifier.size(34.dp)
        )
        Text(
            text = "Aún no tienes plantas",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Agrega tu primera planta para comenzar tu jardín digital",
            color = TextMuted,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
        Button(
            onClick = onAddPlant,
            colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Agregar planta", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SummaryStatCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    accentColor: Color = PlantGreen,
    showAlertCurve: Boolean = false
) {
    val background = if (highlighted) PlantGreen else Color.White
    val contentColor = if (highlighted) Color.White else TextPrimary
    val supportingColor = if (highlighted) Color.White.copy(alpha = 0.9f) else TextMuted

    Box(
        modifier = modifier
            .height(96.dp)
            .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(22.dp))
            .background(background)
    ) {
        if (showAlertCurve) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val path = Path().apply {
                    moveTo(0f, size.height * 0.14f)
                    cubicTo(
                        size.width * 0.08f,
                        size.height * 0.28f,
                        size.width * 0.08f,
                        size.height * 0.72f,
                        0f,
                        size.height * 0.88f
                    )
                    lineTo(0f, size.height * 0.14f)
                }
                drawPath(path, AlertRed.copy(alpha = 0.95f))
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (highlighted) Color.White else accentColor,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = value,
                    color = contentColor,
                    fontSize = if (value.length > 3) 26.sp else 27.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    color = supportingColor,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun PlantsSectionHeader(onViewAllPlants: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Mis plantas",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Ver todas →",
            color = PlantGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onViewAllPlants)
        )
    }
}

@Composable
private fun PlantPreviewCard(
    plant: PlantPreviewUi,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(118.dp)
            .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(start = 14.dp, top = 12.dp, end = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            PlantChip(text = plant.category)
            Column {
                Text(
                    text = plant.name,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "“${plant.subtitle}”",
                    color = TextMuted,
                    fontStyle = FontStyle.Italic,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlantMetric(title = "Salud", value = plant.health, valueColor = PlantGreenDark)
                PlantMetric(
                    title = "Agua",
                    value = plant.watering,
                    valueColor = if (plant.urgentWatering) UrgentOrange else TextPrimary
                )
            }
        }
        PlantIllustration(type = plant.imageType)
    }
}

@Composable
private fun PlantChip(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFE7F6E8))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = text,
            color = PlantGreenDark,
            fontSize = 7.sp,
            lineHeight = 8.sp,
            fontWeight = FontWeight.Black
        )
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(PlantGreen)
        )
    }
}

@Composable
private fun PlantMetric(title: String, value: String, valueColor: Color) {
    Column {
        Text(
            text = title,
            color = TextMuted,
            fontSize = 9.sp,
            lineHeight = 10.sp
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PlantIllustration(type: PlantImageType) {
    Box(
        modifier = Modifier
            .width(92.dp)
            .height(94.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(WarmBeige),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.55f),
                radius = size.minDimension * 0.34f,
                center = Offset(size.width * 0.55f, size.height * 0.48f)
            )
            val potTop = Offset(size.width * 0.35f, size.height * 0.66f)
            drawRoundRect(
                color = Color(0xFFD4B894),
                topLeft = potTop,
                size = Size(size.width * 0.32f, size.height * 0.11f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            drawRoundRect(
                color = Color(0xFFE7D8C3),
                topLeft = Offset(size.width * 0.39f, size.height * 0.74f),
                size = Size(size.width * 0.24f, size.height * 0.16f),
                cornerRadius = CornerRadius(12f, 12f)
            )
            val stemColor = Color(0xFF2E7D32)
            val leafColor = when (type) {
                PlantImageType.Monstera -> Color(0xFF2F7D39)
                PlantImageType.Pothos -> Color(0xFF54A954)
                PlantImageType.Sansevieria -> Color(0xFF3B8A42)
            }

            when (type) {
                PlantImageType.Monstera -> {
                    repeat(8) { index ->
                        val x = size.width * (0.28f + index * 0.065f)
                        val y = size.height * (0.30f + (index % 3) * 0.08f)
                        drawLine(stemColor, Offset(size.width * 0.5f, size.height * 0.68f), Offset(x, y), strokeWidth = 2.6f)
                        drawOval(
                            color = leafColor,
                            topLeft = Offset(x - 14f, y - 10f),
                            size = Size(28f, 21f)
                        )
                    }
                }
                PlantImageType.Pothos -> {
                    repeat(7) { index ->
                        val x = size.width * (0.32f + index * 0.055f)
                        val y = size.height * (0.28f + (index % 4) * 0.11f)
                        drawLine(stemColor, Offset(size.width * 0.5f, size.height * 0.68f), Offset(x, y), strokeWidth = 2.2f)
                        drawOval(
                            color = if (index % 2 == 0) leafColor else Color(0xFF8BCF70),
                            topLeft = Offset(x - 12f, y - 8f),
                            size = Size(24f, 18f)
                        )
                    }
                }
                PlantImageType.Sansevieria -> {
                    repeat(10) { index ->
                        val x = size.width * (0.34f + index * 0.035f)
                        val top = size.height * (0.22f + (index % 3) * 0.03f)
                        val path = Path().apply {
                            moveTo(size.width * 0.5f, size.height * 0.72f)
                            lineTo(x, top)
                            lineTo(x + 9f, size.height * 0.72f)
                            close()
                        }
                        drawPath(path, if (index % 2 == 0) leafColor else Color(0xFF236B35))
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardBottomBar(
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
                val selected = item.route == activeRoute && item.label == "Dashboard"
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(item.route) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 9.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PlantGreen,
                        selectedTextColor = PlantGreen,
                        unselectedIconColor = Color(0xFF8E8E8E),
                        unselectedTextColor = Color(0xFF8E8E8E),
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

private data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun DashboardScreenPreview() {
    ForestCareTheme(dynamicColor = false) {
        DashboardScreen(
            uiState = DashboardUiState(
                plants = listOf(
                    PlantPreviewUi("1", "INTERIOR", "Monstera Deliciosa", "La gigante verde", "Salud Excelente", "En 2 días", imageType = PlantImageType.Monstera),
                    PlantPreviewUi("2", "COLGANTE", "Pothos Marble", "Cascada de Mármol", "Salud Estable", "Riego hoy", urgentWatering = true, imageType = PlantImageType.Pothos),
                    PlantPreviewUi("3", "RESISTENTE", "Sansevieria", "Lengua de Suegra", "Salud Vigorosa", "En 12 días", imageType = PlantImageType.Sansevieria)
                )
            ),
            onAddPlant = {},
            onViewAllPlants = {},
            onPlantClick = {},
            onNavigate = {}
        )
    }
}
