package com.plant.forestcare.ui.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Grass
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.LocalDrink
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.plant.forestcare.navigation.Screen
import com.plant.forestcare.ui.components.ForestCareBottomBar
import com.plant.forestcare.ui.theme.ForestGreen
import com.plant.forestcare.ui.theme.LightBeige

@Composable
fun PlantDetailRoute(
    plantId: String,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onUpdatePhotoClick: () -> Unit,
    onDeleted: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: PlantDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(plantId) {
        viewModel.loadPlant(plantId)
    }
    LaunchedEffect(uiState.deletedSuccessfully) {
        if (uiState.deletedSuccessfully) {
            viewModel.onDeleteNavigationHandled()
            onDeleted()
        }
    }

    PlantDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onEditClick = onEditClick,
        onUpdatePhotoClick = onUpdatePhotoClick,
        onDeleteConfirm = viewModel::deletePlant,
        onWateringDone = viewModel::markWateringDone,
        onNavigate = onNavigate
    )
}

@Composable
fun PlantDetailScreen(
    uiState: PlantDetailUiState,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onUpdatePhotoClick: () -> Unit,
    onDeleteConfirm: () -> Unit,
    onWateringDone: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                onDeleteConfirm()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = LightBeige,
        bottomBar = {
            ForestCareBottomBar(
                activeRoute = Screen.PlantList.route,
                onNavigate = onNavigate
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ForestGreen
                    )
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                uiState.plant == null -> {
                    MissingPlantState(onBackClick)
                }
                else -> {
                    val plant = uiState.plant
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        item {
                            DetailTopBar(
                                plantName = plant.name,
                                location = plant.location,
                                onBackClick = onBackClick,
                                onEditClick = { onEditClick(plant.id) }
                            )
                        }
                        item {
                            PlantHeroCard(plant = plant)
                        }
                        item {
                            DiagnosisTreatmentCard(plant = plant)
                        }
                        if (plant.geminiHealthStatus != null) {
                            item {
                                SectionTitle(text = "Análisis Inteligente")
                            }
                            item {
                                IntelligentAnalysisCard(plant = plant)
                            }
                        }
                        item {
                            SectionTitle(text = "Cuidados Específicos")
                        }
                        item {
                            CareGrid(plant = plant)
                        }
                        item {
                            SectionTitle(text = "Ubicación y Notas")
                        }
                        item {
                            LocationDescriptionCard(plant = plant)
                        }
                        item {
                            SectionTitle(text = "Calendario Mensual")
                        }
                        item {
                            MonthlyCarePlanCard(plant = plant)
                        }
                        item {
                            SectionTitle(text = "Evolución")
                        }
                        item {
                            EvolutionCard()
                        }
                        item {
                            ActionButtons(
                                isDeleting = uiState.isDeleting,
                                onWateringDone = onWateringDone,
                                onEditClick = { onEditClick(plant.id) },
                                onUpdatePhotoClick = onUpdatePhotoClick,
                                onDeleteClick = { showDeleteDialog = true }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MissingPlantState(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No se encontró la planta", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackClick, colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)) {
            Text("Volver")
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = ForestGreen,
        modifier = Modifier.padding(start = 24.dp, top = 32.dp, bottom = 16.dp)
    )
}

@Composable
private fun DetailTopBar(
    plantName: String,
    location: String,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver", tint = ForestGreen)
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = plantName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreen
            )
            StatusBadge(text = location.uppercase(), color = Color(0xFF4CAF50).copy(alpha = 0.1f), textColor = Color(0xFF4CAF50))
        }

        IconButton(
            onClick = onEditClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(Icons.Rounded.Edit, contentDescription = "Editar", tint = ForestGreen)
        }
    }
}

@Composable
private fun EvolutionCard() {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            TimelineItem(
                title = "Siembra",
                date = "15 de Mayo, 2024",
                description = "Planta trasplantada a su maceta definitiva.",
                isLast = false
            )
            TimelineItem(
                title = "Primer brote",
                date = "22 de Mayo, 2024",
                description = "Se observa crecimiento activo en el ápice.",
                isLast = false
            )
            TimelineItem(
                title = "Fertilización",
                date = "01 de Junio, 2024",
                description = "Aplicado abono orgánico líquido.",
                isLast = true
            )
        }
    }
}

@Composable
private fun TimelineItem(
    title: String,
    date: String,
    description: String,
    isLast: Boolean
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(ForestGreen, CircleShape)
            )
            if (!isLast) {
                Spacer(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(ForestGreen.copy(alpha = 0.2f))
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 24.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, color = ForestGreen)
            Text(text = date, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}

@Composable
private fun MonthlyCarePlanCard(plant: PlantDetailUi) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MonthWeek("S1", hasWatering = true, hasFertilizer = false)
                MonthWeek("S2", hasWatering = true, hasFertilizer = true)
                MonthWeek("S3", hasWatering = true, hasFertilizer = false)
                MonthWeek("S4", hasWatering = true, hasFertilizer = false)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = plant.treatmentRecommendation.ifBlank { "Sigue el plan de riego regular para mantener la salud de tu planta." },
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
private fun MonthWeek(label: String, hasWatering: Boolean, hasFertilizer: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            if (hasWatering) {
                Icon(Icons.Rounded.Opacity, "Riego", tint = Color(0xFF2196F3), modifier = Modifier.size(16.dp))
            }
            if (hasFertilizer) {
                Icon(Icons.Rounded.Grass, "Abono", tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun LocationDescriptionCard(plant: PlantDetailUi) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Dashboard, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Ubicación: ${plant.location}", fontWeight = FontWeight.Bold, color = ForestGreen)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = plant.description.ifBlank { "Sin descripción adicional." },
                color = Color.DarkGray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun CareGrid(plant: PlantDetailUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CareInfoCard(
                title = "Luz",
                value = plant.lightRecommendation,
                icon = Icons.Rounded.LightMode,
                color = Color(0xFFFFB300),
                modifier = Modifier.weight(1f)
            )
            CareInfoCard(
                title = "Riego",
                value = plant.currentWateringFrequency,
                icon = Icons.Rounded.Opacity,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CareInfoCard(
                title = "Temperatura",
                value = "18°C - 25°C",
                icon = Icons.Rounded.Thermostat,
                color = Color(0xFFF44336),
                modifier = Modifier.weight(1f)
            )
            CareInfoCard(
                title = "Humedad",
                value = "Media-Alta",
                icon = Icons.Rounded.LocalDrink,
                color = Color(0xFF00BCD4),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CareInfoCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 8.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun IntelligentAnalysisCard(plant: PlantDetailUi) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Notifications, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Estado de Salud", fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = plant.geminiHealthStatus ?: "Tu planta presenta signos de salud excelentes.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Search, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Diagnóstico", fontWeight = FontWeight.Bold, color = ForestGreen)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = plant.geminiDiagnosis ?: plant.diagnosis.ifBlank { "No se detectaron problemas en el último análisis." },
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DiagnosisTreatmentCard(plant: PlantDetailUi) {
    if (plant.diagnosis.isBlank() && plant.treatmentRecommendation.isBlank()) return

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            if (plant.diagnosis.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Search, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Posible Problema", fontWeight = FontWeight.Bold, color = Color.Red)
                }
                Text(text = plant.diagnosis, fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (plant.treatmentRecommendation.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.LocalDrink, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Tratamiento Recomendado", fontWeight = FontWeight.Bold, color = ForestGreen)
                }
                Text(text = plant.treatmentRecommendation, fontSize = 14.sp, color = Color.DarkGray)
            }
        }
    }
}

@Composable
private fun PlantHeroCard(plant: PlantDetailUi) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFFE2EBDC))
    ) {
        if (plant.photoUri != null) {
            AsyncImage(
                model = plant.photoUri,
                contentDescription = plant.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFE2EBDC), Color(0xFFC5D6B6))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                LargePlantIllustration()
            }
        }

        // Floating info card
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .fillMaxWidth(0.85f),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = plant.scientificName.ifBlank { "Especie no identificada" },
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = plant.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(text = plant.healthStatus, color = Color(0xFFE8F5E9), textColor = ForestGreen)
                }
            }
        }
    }
}

@Composable
private fun LargePlantIllustration() {
    Canvas(modifier = Modifier.size(120.dp)) {
        // Simple pot
        drawCircle(
            color = ForestGreen.copy(alpha = 0.2f),
            radius = size.minDimension / 2,
            center = Offset(size.width / 2, size.height / 2)
        )
        // Simple stem
        drawRoundRect(
            color = ForestGreen,
            topLeft = Offset(size.width / 2 - 2, size.height * 0.4f),
            size = Size(4.dp.toPx(), size.height * 0.4f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
        // Simple leaves
        drawRoundRect(
            color = ForestGreen,
            topLeft = Offset(size.width / 2 - 20, size.height * 0.45f),
            size = Size(20.dp.toPx(), 10.dp.toPx()),
            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
        )
    }
}

@Composable
private fun StatusBadge(text: String, color: Color, textColor: Color) {
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun ActionButtons(
    isDeleting: Boolean,
    onWateringDone: () -> Unit,
    onEditClick: () -> Unit,
    onUpdatePhotoClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onWateringDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Icon(Icons.Rounded.Opacity, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Marcar como Regada", fontWeight = FontWeight.Bold)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onUpdatePhotoClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
                Icon(Icons.Rounded.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nueva Foto")
            }

            Button(
                onClick = onDeleteClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = ButtonDefaults.outlinedButtonBorder,
                enabled = !isDeleting
            ) {
                if (isDeleting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Red, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Rounded.Delete, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eliminar", color = Color.Red)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)) {
                Text("Cancelar", color = Color.Black)
            }
        },
        title = { Text("¿Eliminar planta?") },
        text = { Text("Esta acción no se puede deshacer. Se perderá todo el historial de esta planta.") },
        shape = RoundedCornerShape(28.dp),
        containerColor = Color.White
    )
}
