package com.plant.forestcare.ui.form

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.BasicTextField
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.Yard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.plant.forestcare.navigation.Screen
import com.plant.forestcare.ui.components.ForestCareBottomBar
import com.plant.forestcare.ui.theme.ForestCareTheme
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

private val FormBackground = Color(0xFFFBFAF6)
private val CardWhite = Color.White
private val FieldBeige = Color(0xFFF4EFE7)
private val PhotoBeige = Color(0xFFF1ECE3)
private val PlantGreen = Color(0xFF009B3A)
private val PlantGreenDark = Color(0xFF063B1E)
private val PlantGreenSoft = Color(0xFFEAF7EC)
private val TextPrimary = Color(0xFF1B1B1B)
private val TextMuted = Color(0xFF777A73)
private val ChipGreen = Color(0xFFC8F5D2)

@Composable
fun PlantFormRoute(
    navController: NavController,
    plantId: String? = null,
    viewModel: PlantFormViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(plantId) {
        viewModel.loadPlantForEdit(plantId)
    }

    PlantFormScreen(
        uiState = uiState,
        onBack = { navController.popBackStack() },
        onCustomNameChange = viewModel::onCustomNameChange,
        onCommonNameChange = viewModel::onCommonNameChange,
        onScientificNameChange = viewModel::onScientificNameChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onGrowthLocationChange = viewModel::onGrowthLocationChange,
        onSunlightExposureChange = viewModel::onSunlightExposureChange,
        onLeafStatusChange = viewModel::onLeafStatusChange,
        onSoilHumidityChange = viewModel::onSoilHumidityChange,
        onCurrentWateringFrequencyChange = viewModel::onCurrentWateringFrequencyChange,
        onImageSelected = viewModel::onImageSelected,
        onTakePhoto = viewModel::onTakePhoto,
        onIdentifyPlant = viewModel::identifySelectedPlant,
        onGenerateCarePlan = viewModel::generateCarePlan,
        onAddTag = { viewModel.onAddTag() },
        onRemoveTag = viewModel::onRemoveTag,
        onSavePlant = viewModel::savePlant,
        onMessageShown = viewModel::onMessageShown,
        onSaved = {
            viewModel.onNavigationHandled()
            navController.navigate(Screen.PlantList.route) {
                popUpTo(Screen.PlantForm.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        },
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
fun PlantFormScreen(
    uiState: PlantFormUiState,
    onBack: () -> Unit,
    onCustomNameChange: (String) -> Unit,
    onCommonNameChange: (String) -> Unit,
    onScientificNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onGrowthLocationChange: (String) -> Unit,
    onSunlightExposureChange: (String) -> Unit,
    onLeafStatusChange: (String) -> Unit,
    onSoilHumidityChange: (String) -> Unit,
    onCurrentWateringFrequencyChange: (String) -> Unit,
    onImageSelected: (Uri?) -> Unit,
    onTakePhoto: (Uri?) -> Unit,
    onIdentifyPlant: () -> Unit,
    onGenerateCarePlan: () -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit,
    onSavePlant: () -> Unit,
    onMessageShown: () -> Unit,
    onSaved: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = onImageSelected
    )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onTakePhoto(pendingCameraUri)
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createCameraImageUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        }
    }
    val launchCamera = {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val uri = createCameraImageUri(context)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onMessageShown()
        }
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            Log.d("PlantFormFlow", "[FLOW][NAVIGATION] Guardado confirmado desde formulario, navegando a Lista de Plantas")
            snackbarHostState.showSnackbar(
                message = "Planta guardada correctamente",
                duration = SnackbarDuration.Short
            )
            onMessageShown()
            onSaved()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FormBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            ForestCareBottomBar(
                activeRoute = Screen.PlantForm.route,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                AddPlantHeader(
                    title = if (uiState.isEditMode) "Editar planta" else "Agregar planta",
                    onBack = onBack
                )
            }
            item {
                PlantPhotoUploadBox(
                    imageUri = uiState.selectedImageUri,
                    onUploadImage = { imagePicker.launch("image/*") },
                    onTakePhoto = launchCamera
                )
            }
            item {
                FormProgressSteps(uiState = uiState)
            }
            item {
                PlantIdentificationSection(
                    uiState = uiState,
                    onIdentifyPlant = onIdentifyPlant
                )
            }
            item {
                PlantInformationSection(
                    uiState = uiState,
                    onCustomNameChange = onCustomNameChange,
                    onCommonNameChange = onCommonNameChange,
                    onScientificNameChange = onScientificNameChange,
                    onDescriptionChange = onDescriptionChange
                )
            }
            item {
                EnvironmentSection(
                    uiState = uiState,
                    onGrowthLocationChange = onGrowthLocationChange,
                    onSunlightExposureChange = onSunlightExposureChange,
                    onLeafStatusChange = onLeafStatusChange,
                    onSoilHumidityChange = onSoilHumidityChange,
                    onCurrentWateringFrequencyChange = onCurrentWateringFrequencyChange,
                    onAddTag = onAddTag,
                    onRemoveTag = onRemoveTag
                )
            }
            item {
                CarePlanSection(
                    uiState = uiState,
                    onGenerateCarePlan = onGenerateCarePlan
                )
            }
            item {
                SavePlantButton(
                    isLoading = uiState.isSaving,
                    isAnalyzing = uiState.isAnalyzing || uiState.isIdentifying || uiState.isDiseaseAnalyzing || uiState.isGeminiAnalyzing,
                    isEditMode = uiState.isEditMode,
                    onClick = onSavePlant
                )
            }
        }
    }
}

@Composable
private fun PlantIdentificationSection(
    uiState: PlantFormUiState,
    onIdentifyPlant: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(icon = Icons.Rounded.CameraAlt, text = "Foto")
        FormCard {
            Text(
                text = "Sube una foto clara de tu planta",
                color = TextMuted,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            Button(
                onClick = onIdentifyPlant,
                enabled = uiState.selectedImageUri != null &&
                    !uiState.isAnalyzing &&
                    !uiState.isIdentifying &&
                    !uiState.isDiseaseAnalyzing &&
                    !uiState.isGeminiAnalyzing &&
                    !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PlantGreen,
                    contentColor = Color.White
                )
            ) {
                if (uiState.isIdentifying || uiState.isDiseaseAnalyzing || uiState.isGeminiAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = when {
                            uiState.isIdentifying -> "Revisando tu planta..."
                            uiState.isDiseaseAnalyzing -> "Revisando su salud..."
                            else -> "Preparando tu recomendación..."
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Revisar planta", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            uiState.identificationMessage?.let { message ->
                Text(
                    text = message,
                    color = PlantGreenDark,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            uiState.identificationErrorMessage?.let { message ->
                Text(
                    text = message,
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
            uiState.identificationResult?.let { result ->
                Text(
                    text = "Creemos que es: ${result.commonName}",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
            uiState.diseaseAnalysisMessage?.let { message ->
                Text(
                    text = message,
                    color = PlantGreenDark,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            uiState.diseaseAnalysisErrorMessage?.let { message ->
                Text(
                    text = message,
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
            uiState.diseaseDiagnosisResult?.let { disease ->
                Text(
                    text = "Estado de salud: ${disease.diseaseName ?: "sin señales importantes"}",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
                disease.treatmentRecommendation?.let { treatment ->
                    Text(
                        text = "Tratamiento inicial: $treatment",
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
            if (uiState.isGeminiAnalyzing) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = PlantGreen
                    )
                    Text(
                        text = "Preparando tu recomendación...",
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
            uiState.geminiAnalysisMessage?.let { message ->
                Text(
                    text = message,
                    color = PlantGreenDark,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            uiState.geminiAnalysisErrorMessage?.let { message ->
                Text(
                    text = message,
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
            uiState.geminiAnalysisResult?.let { analysis ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(PlantGreenSoft)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Recomendación para tu planta",
                        color = PlantGreenDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = analysis.diagnosis.ifBlank { "Tenemos un plan básico listo para ti." },
                        color = TextPrimary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                    if (analysis.possibleCauses.isNotEmpty()) {
                        Text(
                            text = "Causas: ${analysis.possibleCauses.take(3).joinToString(", ")}",
                            color = TextMuted,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                    Text(
                        text = "Riesgo: ${analysis.riskLevel}",
                        color = PlantGreenDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun FormProgressSteps(uiState: PlantFormUiState) {
    val steps = listOf(
        "Foto" to (uiState.selectedImageUri != null),
        "Análisis" to (uiState.identificationResult != null || uiState.diseaseDiagnosisResult != null || uiState.geminiAnalysisResult != null),
        "Confirmar" to (uiState.customName.isNotBlank() && uiState.commonName.isNotBlank()),
        "Guardar" to uiState.saveSuccess
    )
    FormCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            steps.forEachIndexed { index, (label, completed) ->
                val active = when (index) {
                    0 -> uiState.selectedImageUri == null
                    1 -> uiState.isAnalyzing || uiState.isIdentifying || uiState.isDiseaseAnalyzing || uiState.isGeminiAnalyzing
                    2 -> !uiState.isSaving && !uiState.saveSuccess && (uiState.commonName.isNotBlank() || uiState.customName.isNotBlank())
                    else -> uiState.isSaving || uiState.saveSuccess
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    completed -> PlantGreen
                                    active -> PlantGreenSoft
                                    else -> FieldBeige
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            color = if (completed) Color.White else PlantGreenDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = label,
                        color = if (active || completed) PlantGreenDark else TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun AddPlantHeader(
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Volver",
                tint = PlantGreenDark,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFD9E9C8), Color(0xFF8E6D4E))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("M", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PlantPhotoUploadBox(
    imageUri: String?,
    onUploadImage: () -> Unit,
    onTakePhoto: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(258.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(PhotoBeige)
            .clickable(onClick = onUploadImage),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.18f))
            )
        }
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = Color(0xFFB7C7B0),
                style = Stroke(
                    width = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx(), 28.dp.toPx())
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.CameraAlt,
                    contentDescription = null,
                    tint = PlantGreenDark,
                    modifier = Modifier.size(27.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = if (imageUri == null) "Captura tu planta" else "Foto seleccionada",
                color = if (imageUri == null) TextPrimary else Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Toca para subir una foto",
                color = if (imageUri == null) TextMuted else Color.White,
                fontSize = 10.sp
            )
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onUploadImage,
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = PlantGreenDark
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("Subir imagen", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onTakePhoto,
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PlantGreen,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("Tomar foto", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PlantInformationSection(
    uiState: PlantFormUiState,
    onCustomNameChange: (String) -> Unit,
    onCommonNameChange: (String) -> Unit,
    onScientificNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(icon = Icons.Rounded.Spa, text = "Datos simples")
        FormCard {
            RoundedInputField(
                label = "Nombre de cariño (opcional)",
                value = uiState.customName,
                placeholder = "Ej. Monstera de Mateo",
                onValueChange = onCustomNameChange
            )
            if (uiState.commonName.isBlank() && uiState.scientificName.isBlank()) {
                Text(
                    text = "Puedes revisar una foto o escribir el nombre de la planta.",
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
                RoundedInputField(
                    label = "Nombre de la planta",
                    value = uiState.commonName,
                    placeholder = "Ej. Monstera, Cactus, Rosal...",
                    onValueChange = onCommonNameChange
                )
            } else {
                AutoDetectedInfo(
                    commonName = uiState.commonName,
                    scientificName = uiState.scientificName,
                    description = uiState.description
                )
            }
        }
    }
}

@Composable
private fun AutoDetectedInfo(
    commonName: String,
    scientificName: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(PlantGreenSoft)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("Creemos que es esta planta", color = PlantGreenDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(commonName.ifBlank { "Pendiente" }, color = TextPrimary, fontSize = 11.sp)
        Text(scientificName.ifBlank { "Nombre científico pendiente" }, color = TextPrimary, fontSize = 11.sp, fontStyle = FontStyle.Italic)
        if (description.isNotBlank()) {
            Text(description, color = TextMuted, fontSize = 11.sp, lineHeight = 15.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun EnvironmentSection(
    uiState: PlantFormUiState,
    onGrowthLocationChange: (String) -> Unit,
    onSunlightExposureChange: (String) -> Unit,
    onLeafStatusChange: (String) -> Unit,
    onSoilHumidityChange: (String) -> Unit,
    onCurrentWateringFrequencyChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(icon = Icons.Rounded.Yard, text = "Cuidados actuales")
        FormCard {
            FieldLabel("Ubicación de crecimiento")
            GrowthLocationSelector(
                selected = uiState.growthLocation,
                onSelected = onGrowthLocationChange
            )
            Spacer(Modifier.height(4.dp))
            FieldLabel("Exposición solar")
            SunlightExposureSelector(
                selected = uiState.sunlightExposure,
                onSelected = onSunlightExposureChange
            )
            Spacer(Modifier.height(4.dp))
            FieldLabel("Estado de hojas")
            CompactOptionSelector(
                options = listOf("Sanas", "Amarillas", "Secas", "Caídas", "Con manchas"),
                selected = uiState.leafStatus,
                onSelected = onLeafStatusChange
            )
            Spacer(Modifier.height(4.dp))
            FieldLabel("Humedad de tierra")
            CompactOptionSelector(
                options = listOf("Seca", "Húmeda", "Muy mojada", "No sé"),
                selected = uiState.soilHumidity,
                onSelected = onSoilHumidityChange
            )
            Spacer(Modifier.height(4.dp))
            FieldLabel("Frecuencia actual de riego")
            CompactOptionSelector(
                options = listOf("Diario", "Cada 2 días", "Semanal", "No recuerdo"),
                selected = uiState.currentWateringFrequency,
                onSelected = onCurrentWateringFrequencyChange
            )
            Spacer(Modifier.height(4.dp))
            FieldLabel("Etiquetas")
            TagChipsSection(
                tags = uiState.tags,
                onAddTag = onAddTag,
                onRemoveTag = onRemoveTag
            )
        }
    }
}

@Composable
private fun SectionTitle(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PlantGreen,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            color = PlantGreenDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun FormCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .shadow(10.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black.copy(alpha = 0.06f))
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

@Composable
private fun RoundedInputField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    minHeight: androidx.compose.ui.unit.Dp = 48.dp,
    singleLine: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        FieldLabel(label)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = TextStyle(
                color = TextPrimary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(minHeight)
                .clip(RoundedCornerShape(20.dp))
                .background(FieldBeige)
                .padding(horizontal = 16.dp, vertical = if (singleLine) 0.dp else 13.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFF8D8B84),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = TextPrimary,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun GrowthLocationSelector(
    selected: String,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(50))
            .background(FieldBeige)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        listOf("Interior", "Exterior", "Balcón", "Cerca de ventana").forEach { option ->
            val active = selected == option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(50))
                    .background(if (active) Color(0xFF008A2E) else Color.Transparent)
                    .clickable { onSelected(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (active) Color.White else TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SunlightExposureSelector(
    selected: String,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SunlightOption("Sol directo", Icons.Rounded.WbSunny, selected == "Sol directo", Modifier.weight(1f), onSelected)
        SunlightOption("Luz indirecta", Icons.Rounded.WbSunny, selected == "Luz indirecta", Modifier.weight(1f), onSelected)
        SunlightOption("Poca luz", Icons.Rounded.KeyboardArrowDown, selected == "Poca luz", Modifier.weight(1f), onSelected)
        SunlightOption("No sabe", Icons.Rounded.KeyboardArrowDown, selected == "No sabe", Modifier.weight(1f), onSelected)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CompactOptionSelector(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val active = selected == option
            TagChip(
                text = option,
                background = if (active) PlantGreenSoft else FieldBeige,
                contentColor = if (active) PlantGreenDark else TextMuted,
                onClick = { onSelected(option) }
            )
        }
    }
}

@Composable
private fun CarePlanSection(
    uiState: PlantFormUiState,
    onGenerateCarePlan: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(icon = Icons.Rounded.Spa, text = "Guardar")
        FormCard {
            Button(
                onClick = onGenerateCarePlan,
                enabled = uiState.commonName.isNotBlank() || uiState.scientificName.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PlantGreen,
                    contentColor = Color.White
                )
            ) {
                Text("Preparar plan de cuidado", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            AnimatedVisibility(visible = uiState.carePlan != null) {
                val plan = uiState.carePlan ?: return@AnimatedVisibility
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(PlantGreenSoft)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text("Tu plan de cuidado está listo", color = PlantGreenDark, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(plan.diagnosis, color = TextPrimary, fontSize = 11.sp, lineHeight = 15.sp)
                    Text("Riego: ${plan.wateringRecommendation}", color = TextPrimary, fontSize = 11.sp, lineHeight = 15.sp)
                    Text("Luz: ${plan.lightRecommendation}", color = TextPrimary, fontSize = 11.sp, lineHeight = 15.sp)
                    Text("Fertilización: ${plan.fertilizationRecommendation}", color = TextMuted, fontSize = 11.sp, lineHeight = 15.sp)
                    Text("Poda: ${plan.pruningRecommendation}", color = TextMuted, fontSize = 11.sp, lineHeight = 15.sp)
                    Text("Tratamiento: ${plan.treatmentRecommendation}", color = TextMuted, fontSize = 11.sp, lineHeight = 15.sp)
                    Text("Próxima revisión: en ${plan.nextReviewDays} días", color = PlantGreenDark, fontSize = 11.sp, lineHeight = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun SunlightOption(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    modifier: Modifier,
    onSelected: (String) -> Unit
) {
    Column(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) PlantGreenSoft else Color(0xFFF7F3EC))
            .border(
                width = if (selected) 1.5.dp else 0.dp,
                color = if (selected) PlantGreen else Color.Transparent,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onSelected(label) }
            .padding(vertical = 9.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) PlantGreen else Color(0xFFB1A68F),
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = label,
            color = if (selected) PlantGreenDark else TextPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagChipsSection(
    tags: List<String>,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            TagChip(
                text = "$tag ×",
                background = ChipGreen,
                contentColor = PlantGreenDark,
                onClick = { onRemoveTag(tag) }
            )
        }
        TagChip(
            text = "+ Agregar etiqueta",
            background = FieldBeige,
            contentColor = TextMuted,
            onClick = onAddTag
        )
    }
}

@Composable
private fun TagChip(
    text: String,
    background: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(50))
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SavePlantButton(
    isLoading: Boolean,
    isAnalyzing: Boolean,
    isEditMode: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !isLoading && !isAnalyzing,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(29.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF008D2F),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 18.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.CreditCard,
            contentDescription = null,
            modifier = Modifier.size(17.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = when {
                isLoading -> "Guardando planta..."
                isEditMode -> "Actualizar planta"
                else -> "Guardar planta"
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PlantFormBottomBar(
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
                            modifier = Modifier.size(19.dp)
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

private fun createCameraImageUri(context: Context): Uri {
    val imagesDir = File(context.cacheDir, "images").apply {
        mkdirs()
    }
    val imageFile = File.createTempFile("plant_", ".jpg", imagesDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun PlantFormScreenPreview() {
    ForestCareTheme(dynamicColor = false) {
        PlantFormScreen(
            uiState = PlantFormUiState(),
            onBack = {},
            onCustomNameChange = {},
            onCommonNameChange = {},
            onScientificNameChange = {},
            onDescriptionChange = {},
            onGrowthLocationChange = {},
            onSunlightExposureChange = {},
            onLeafStatusChange = {},
            onSoilHumidityChange = {},
            onCurrentWateringFrequencyChange = {},
            onImageSelected = {},
            onTakePhoto = {},
            onIdentifyPlant = {},
            onGenerateCarePlan = {},
            onAddTag = {},
            onRemoveTag = {},
            onSavePlant = {},
            onMessageShown = {},
            onSaved = {},
            onNavigate = {}
        )
    }
}
