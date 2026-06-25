package com.plant.forestcare.ui.detail

<<<<<<< HEAD
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plant.forestcare.utils.Constants

@Composable
fun PlantDetailScreen(
    plantId: String,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    val plant = Constants.fakePlants.find { it.id == plantId } ?: return

    Scaffold(
        containerColor = Color(0xFFF6F1EA),
        bottomBar = {
            BottomNavigationBar()
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Header: Back arrow, Centered name and location
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Text("←", fontSize = 24.sp, color = Color(0xFF2D4739))
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = plant.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D4739)
                        )
                        Text(
                            text = "EN EL ${plant.location.uppercase()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Hero Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFFE2EBDC))
                ) {
                    // Floating card on image (bottom-left)
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.9f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Monty", // Nickname example
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                            Text(
                                text = plant.scientificName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D4739)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = Color(0xFFE8F1E5),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = plant.healthStatus,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Care Grid 2x2
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CareDetailCard(
                            title = "RIEGO",
                            value = "${plant.healthPercent}%",
                            subtitle = plant.wateringInfo,
                            icon = "💧",
                            modifier = Modifier.weight(1f)
                        )
                        CareDetailCard(
                            title = "LUZ",
                            value = plant.lightInfo,
                            subtitle = "Brillante",
                            icon = "☀️",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CareDetailCard(
                            title = "ABONO",
                            value = plant.fertilizerInfo,
                            subtitle = "Cada mes",
                            icon = "🧪",
                            modifier = Modifier.weight(1f)
                        )
                        CareDetailCard(
                            title = "PODA",
                            value = plant.pruningInfo,
                            subtitle = "Hojas secas",
                            icon = "✂️",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Quick Actions
                Text(
                    text = "Acciones rápidas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D4739)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D4739))
                    ) {
                        Text("Regar ahora", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Text("Registrar cuidado", color = Color(0xFF2D4739), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Care History
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Historial de cuidado",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D4739)
                    )
                    Text(
                        text = "VER TODO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Timeline
                Column {
                    TimelineItem("Riego completo", "Humedad del suelo al 90%", isLast = false)
                    TimelineItem("Fertilización orgánica", "NPK 10-10-10 aplicado", isLast = false)
                    TimelineItem("Nueva foto añadida", "Ver galería de crecimiento", isLast = true, hasImage = true)
                }

                Spacer(modifier = Modifier.height(40.dp))
=======
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ContentCut
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Grass
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.plant.forestcare.navigation.Screen
import com.plant.forestcare.ui.components.ForestCareBottomBar

private val PlantBackground = Color(0xFFF8FAF7)
private val PlantGreen = Color(0xFF2E7D32)
private val PlantGreenDark = Color(0xFF1B5E20)
private val PlantGreenStrong = Color(0xFF2E7D32)
private val TextPrimary = Color(0xFF1B1B1B)
private val TextMuted = Color(0xFF616161)
private val WarmBeige = Color(0xFFF3EAD9)
private val AlertRed = Color(0xFFE53935)
private val UrgentOrange = Color(0xFFF9A825)

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
    val plant = uiState.plant

    if (showDeleteDialog && plant != null) {
        DeleteConfirmationDialog(
            plantName = plant.name,
            isDeleting = uiState.isDeleting,
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDeleteConfirm()
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        bottomBar = {
            ForestCareBottomBar(
                activeRoute = Screen.PlantList.route,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF123B25), Color(0xFFE8F5E9), PlantBackground)
                    )
                )
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PlantGreen)
                    }
                }
                plant == null -> {
                    MissingPlantState(
                        message = uiState.errorMessage ?: "No encontramos esta planta",
                        onBackClick = onBackClick,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 22.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            DetailTopBar(
                                plant = plant,
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
                        if (plant.hasGeminiAnalysis()) {
                            item {
                                SectionTitle(text = "Consejos")
                            }
                            item {
                                IntelligentAnalysisCard(plant = plant)
                            }
                        }
                        item {
                            SectionTitle(text = "Próximo cuidado")
                        }
                        item {
                            CareGrid(plant = plant)
                        }
                        item {
                            SectionTitle(text = "Historial")
                        }
                        item {
                            LocationDescriptionCard(plant = plant)
                        }
                        item {
                            SectionTitle(text = "Plan de 30 días")
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
>>>>>>> 37077b2d263d6d6a56e89dc317baeb99661867ce
            }
        }
    }
}

@Composable
<<<<<<< HEAD
fun CareDetailCard(
    title: String,
    value: String,
    subtitle: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D4739))
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun TimelineItem(
    title: String,
    description: String,
    isLast: Boolean,
    hasImage: Boolean = false
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFE0E0E0))
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D4739))
            Text(text = description, fontSize = 12.sp, color = Color.Gray)
            if (hasImage) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE2EBDC))
                )
=======
private fun EvolutionCard() {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatusBadge(text = "Igual")
            Text("Actualiza con una nueva foto para comparar su progreso.", color = TextMuted, fontSize = 12.sp, lineHeight = 17.sp)
        }
    }
}

@Composable
private fun MonthlyCarePlanCard(plant: PlantDetailUi) {
    if (plant.geminiWeeklyCarePlan.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            plant.geminiWeeklyCarePlan.take(4).forEachIndexed { index, task ->
                MonthWeek(
                    title = "Semana ${index + 1}",
                    tasks = listOf(task)
                )
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        MonthWeek(
            title = "Semana 1",
            tasks = listOf(
                plant.treatmentRecommendationText(),
                "Mantener ${plant.sunlightExposure.lowercase()}",
                "Revisar hojas afectadas"
            )
        )
        MonthWeek(
            title = "Semana 2",
            tasks = listOf(
                "Limpiar hojas con cuidado",
                "Revisar humedad del suelo",
                "Confirmar próximo riego: ${plant.nextWateringText}"
            )
        )
        MonthWeek(
            title = "Semana 3",
            tasks = listOf(
                plant.pruningRecommendation,
                plant.fertilizationRecommendation,
                "Verificar crecimiento nuevo"
            )
        )
        MonthWeek(
            title = "Semana 4",
            tasks = listOf(
                "Tomar nueva foto de control",
                "Comparar progreso con la revisión inicial",
                "Próxima revisión: ${plant.nextReviewText}"
            )
        )
    }
}

@Composable
private fun MonthWeek(
    title: String,
    tasks: List<String>
) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(title, color = PlantGreenDark, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        tasks.forEach { task ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(PlantGreen)
                )
                Text(task, color = TextMuted, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
    }
}

private fun PlantDetailUi.treatmentRecommendationText(): String {
    return diseaseTreatmentRecommendation
        ?: geminiTreatmentRecommendation
        ?: treatmentRecommendation
        ?: "Mantener rutina recomendada"
}

private fun PlantDetailUi.hasGeminiAnalysis(): Boolean {
    return !geminiDiagnosis.isNullOrBlank() ||
        geminiPossibleCauses.isNotEmpty() ||
        !geminiTreatmentRecommendation.isNullOrBlank() ||
        geminiWeeklyCarePlan.isNotEmpty()
}

@Composable
private fun IntelligentAnalysisCard(plant: PlantDetailUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        plant.geminiHealthStatus?.takeIf { it.isNotBlank() }?.let { status ->
            StatusBadge(text = "Riesgo ${plant.geminiRiskLevel ?: status}")
        }
        Text(
            text = plant.geminiDiagnosis ?: "Consejo no disponible por ahora.",
            color = TextMuted,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
        if (plant.geminiPossibleCauses.isNotEmpty()) {
            Text("Posibles causas", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            plant.geminiPossibleCauses.take(4).forEach { cause ->
                Text("• $cause", color = TextMuted, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
        plant.geminiTreatmentRecommendation?.takeIf { it.isNotBlank() }?.let { treatment ->
            Text("Consejo personalizado", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(treatment, color = TextMuted, fontSize = 12.sp, lineHeight = 17.sp)
        }
    }
}

@Composable
private fun DiagnosisTreatmentCard(plant: PlantDetailUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Estado actual", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(plant.diagnosis, color = TextMuted, fontSize = 13.sp, lineHeight = 17.sp)
        plant.diseaseName?.let { diseaseName ->
            Text(
                text = "Revisar: $diseaseName",
                color = AlertRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text("Qué hacer ahora", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(
            text = plant.diseaseTreatmentRecommendation
                ?: plant.treatmentRecommendation.ifBlank { "Mantén la rutina recomendada y revisa cambios en las hojas." },
            color = TextMuted,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
        Text("Próxima revisión: ${plant.nextReviewText}", color = PlantGreenDark, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun DetailTopBar(
    plant: PlantDetailUi,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Volver",
                tint = PlantGreenDark,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = plant.name,
                color = TextPrimary,
                fontSize = 18.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (plant.scientificName.isNotBlank()) {
                Text(
                    text = plant.scientificName,
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFE7F6E8))
        ) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = "Editar planta",
                tint = PlantGreen,
                modifier = Modifier.size(19.dp)
            )
        }
    }
}

@Composable
private fun PlantHeroCard(plant: PlantDetailUi) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .shadow(8.dp, RoundedCornerShape(28.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(28.dp))
            .background(WarmBeige)
    ) {
        if (plant.photoUri != null) {
            AsyncImage(
                model = plant.photoUri,
                contentDescription = plant.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.32f))
                        )
                    )
            )
        } else {
            LargePlantIllustration()
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp),
            color = Color.White.copy(alpha = 0.94f),
            shape = RoundedCornerShape(22.dp),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = plant.diagnosis,
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = plant.name,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                StatusBadge(text = plant.healthStatus)
                plant.diseaseName?.let { diseaseName ->
                    StatusBadge(text = diseaseName)
                }
            }
        }
    }
}

@Composable
private fun LargePlantIllustration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.White.copy(alpha = 0.55f),
            radius = size.minDimension * 0.28f,
            center = Offset(size.width * 0.55f, size.height * 0.45f)
        )
        drawRoundRect(
            color = Color(0xFFD4B894),
            topLeft = Offset(size.width * 0.39f, size.height * 0.68f),
            size = Size(size.width * 0.22f, size.height * 0.08f),
            cornerRadius = CornerRadius(16f, 16f)
        )
        drawRoundRect(
            color = Color(0xFFE7D8C3),
            topLeft = Offset(size.width * 0.42f, size.height * 0.75f),
            size = Size(size.width * 0.16f, size.height * 0.12f),
            cornerRadius = CornerRadius(18f, 18f)
        )
        repeat(10) { index ->
            val x = size.width * (0.26f + index * 0.055f)
            val y = size.height * (0.20f + (index % 4) * 0.08f)
            drawLine(
                color = Color(0xFF2E7D32),
                start = Offset(size.width * 0.5f, size.height * 0.70f),
                end = Offset(x, y),
                strokeWidth = 4f
            )
            drawOval(
                color = if (index % 2 == 0) Color(0xFF2F7D39) else Color(0xFF54A954),
                topLeft = Offset(x - 24f, y - 16f),
                size = Size(48f, 34f)
            )
        }
    }
}

@Composable
private fun StatusBadge(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFE7F6E8))
            .padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(PlantGreen)
        )
        Text(text = text, color = PlantGreenDark, fontSize = 10.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = TextPrimary,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun CareGrid(plant: PlantDetailUi) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CareInfoCard(
                icon = Icons.Rounded.WaterDrop,
                title = "Riego",
                value = "Cada ${plant.recommendedWateringDays} días",
                subtitle = "Próximo: ${plant.nextWateringText}",
                highlighted = plant.nextWateringText.equals("Hoy", ignoreCase = true),
                modifier = Modifier.weight(1f)
            )
            CareInfoCard(
                icon = Icons.Rounded.LightMode,
                title = "Luz",
                value = plant.sunlightExposure,
                subtitle = plant.lightRecommendation,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CareInfoCard(
                icon = Icons.Rounded.Science,
                title = "Abono",
                value = "Plan",
                subtitle = plant.fertilizationRecommendation,
                modifier = Modifier.weight(1f)
            )
            CareInfoCard(
                icon = Icons.Rounded.ContentCut,
                title = "Poda",
                value = "Revisar",
                subtitle = plant.pruningRecommendation,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CareInfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false
) {
    val background = if (highlighted) PlantGreen else Color.White
    val contentColor = if (highlighted) Color.White else TextPrimary
    val supportingColor = if (highlighted) Color.White.copy(alpha = 0.9f) else TextMuted
    val iconColor = if (highlighted) Color.White else if (title == "Riego") UrgentOrange else PlantGreen

    Column(
        modifier = modifier
            .height(112.dp)
            .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(22.dp))
            .background(background)
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(21.dp))
        Column {
            Text(text = value, color = contentColor, fontSize = 17.sp, lineHeight = 19.sp, fontWeight = FontWeight.Bold)
            Text(text = title, color = supportingColor, fontSize = 10.sp, lineHeight = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = supportingColor, fontSize = 9.sp, lineHeight = 11.sp)
        }
    }
}

@Composable
private fun LocationDescriptionCard(plant: PlantDetailUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = PlantGreen, modifier = Modifier.size(20.dp))
            Column {
                Text("Ubicación", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                Text(plant.location, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(Icons.Rounded.WaterDrop, contentDescription = null, tint = PlantGreen, modifier = Modifier.size(20.dp))
            Column {
                Text("Condición actual", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    "${plant.leafStatus} · Tierra ${plant.soilHumidity.lowercase()} · Riego ${plant.currentWateringFrequency.lowercase()}",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        plant.diseaseName?.let { diseaseName ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Rounded.Science, contentDescription = null, tint = AlertRed, modifier = Modifier.size(20.dp))
                Column {
                    Text("Necesita revisión", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        diseaseName,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        plant.diseaseDescription ?: plant.diseaseTreatmentRecommendation ?: "Revisar evolución ${plant.nextReviewText}.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(Icons.Rounded.Grass, contentDescription = null, tint = PlantGreen, modifier = Modifier.size(20.dp))
            Column {
                Text("Etiquetas", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = plant.tags.ifEmpty { listOf("Sin etiquetas") }.joinToString(" · "),
                    color = TextPrimary,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(
            text = plant.description.ifBlank { "Aún no hay descripción para esta planta." },
            color = TextMuted,
            fontSize = 12.sp,
            lineHeight = 17.sp
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = onWateringDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(27.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PlantGreenStrong)
        ) {
            Icon(Icons.Rounded.WaterDrop, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Marcar cuidado realizado", fontWeight = FontWeight.Bold)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onEditClick,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp)
            ) {
                Icon(Icons.Rounded.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Editar", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = onUpdatePhotoClick,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp)
            ) {
                Icon(Icons.Rounded.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Actualizar con nueva foto", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onDeleteClick,
                enabled = !isDeleting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRed)
            ) {
                Icon(Icons.Rounded.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(if (isDeleting) "Eliminando..." else "Eliminar", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    plantName: String,
    isDeleting: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar planta") },
        text = { Text("¿Quieres eliminar $plantName? Esta acción no se puede deshacer.") },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = !isDeleting) {
                Text("Eliminar", color = AlertRed, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isDeleting) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun MissingPlantState(
    message: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
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
            Icon(Icons.Rounded.Spa, contentDescription = null, tint = PlantGreen, modifier = Modifier.size(34.dp))
            Text("Planta no disponible", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(message, color = TextMuted, fontSize = 12.sp, lineHeight = 16.sp)
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Volver", color = Color.White, fontWeight = FontWeight.Bold)
>>>>>>> 37077b2d263d6d6a56e89dc317baeb99661867ce
            }
        }
    }
}

@Composable
<<<<<<< HEAD
private fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("Home", "🏠", false),
            Triple("Plants", "🌿", true),
            Triple("Dashboard", "📊", false),
            Triple("Reminders", "🔔", false),
            Triple("Profile", "👤", false)
        )

        items.forEach { (title, icon, isSelected) ->
            NavigationBarItem(
                selected = isSelected,
                onClick = { },
                icon = {
                    Text(icon, fontSize = 20.sp)
                },
                label = {
                    Text(
                        title,
                        fontSize = 10.sp,
                        color = if (isSelected) Color(0xFF2D4739) else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFFE2EBDC)
                )
            )
        }
    }
}
=======
private fun DetailBottomBar(
    activeRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("Inicio", Screen.Home.route, Icons.Rounded.Home),
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
                    selected = item.route == activeRoute && item.label == "Plantas",
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
>>>>>>> 37077b2d263d6d6a56e89dc317baeb99661867ce
