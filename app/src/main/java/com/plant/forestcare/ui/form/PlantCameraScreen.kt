package com.plant.forestcare.ui.form

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.plant.forestcare.navigation.Screen
import java.io.File

private val CameraGreen = Color(0xFF2E7D32)
private val CameraGreenDark = Color(0xFF1B5E20)
private val CameraBackground = Color(0xFFF8FAF7)
private val CameraBeige = Color(0xFFF3EAD9)
private val TextMuted = Color(0xFF616161)
private val TextPrimary = Color(0xFF1B1B1B)

private enum class PlantCaptureStep {
    Camera,
    Loading,
    Review
}

@Composable
fun PlantCameraRoute(
    navController: NavController,
    viewModel: PlantFormViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var step by remember { mutableStateOf(PlantCaptureStep.Camera) }

    LaunchedEffect(uiState.selectedImageUri) {
        if (!uiState.selectedImageUri.isNullOrBlank() && step == PlantCaptureStep.Camera) {
            step = PlantCaptureStep.Loading
            viewModel.identifySelectedPlant()
        }
    }

    LaunchedEffect(
        uiState.isAnalyzing,
        uiState.isIdentifying,
        uiState.isDiseaseAnalyzing,
        uiState.isGeminiAnalyzing,
        uiState.carePlanGenerated,
        uiState.identificationResult
    ) {
        val finished = !uiState.isAnalyzing &&
            !uiState.isIdentifying &&
            !uiState.isDiseaseAnalyzing &&
            !uiState.isGeminiAnalyzing
        if (step == PlantCaptureStep.Loading && finished && (uiState.carePlanGenerated || uiState.identificationResult != null)) {
            if (uiState.commonName.isBlank()) {
                viewModel.onCommonNameChange("Planta")
            }
            if (uiState.customName.isBlank()) {
                viewModel.onCustomNameChange(uiState.commonName.ifBlank { "Mi planta" })
            }
            step = PlantCaptureStep.Review
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onMessageShown()
        }
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            Log.d("PlantCameraFlow", "[FLOW][NAVIGATION] Guardado confirmado, navegando a Lista de Plantas")
            snackbarHostState.showSnackbar(
                message = "Planta guardada correctamente",
                duration = SnackbarDuration.Short
            )
            viewModel.onMessageShown()
            viewModel.onNavigationHandled()
            navController.navigate(Screen.PlantList.route) {
                popUpTo(Screen.PlantCamera.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    androidx.compose.material3.Scaffold(
        containerColor = CameraBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Crossfade(targetState = step, label = "plant_capture_flow", modifier = Modifier.padding(padding)) { current ->
            when (current) {
                PlantCaptureStep.Camera -> PlantCameraScreen(
                    onBack = { navController.popBackStack() },
                    onImageSelected = viewModel::onImageSelected
                )
                PlantCaptureStep.Loading -> PlantAnalysisLoadingScreen(
                    imageUri = uiState.selectedImageUri,
                    isIdentifying = uiState.isIdentifying,
                    isCheckingHealth = uiState.isDiseaseAnalyzing,
                    isPreparingPlan = uiState.isGeminiAnalyzing || uiState.carePlanGenerated
                )
                PlantCaptureStep.Review -> PlantReviewScreen(
                    uiState = uiState,
                    onCustomNameChange = viewModel::onCustomNameChange,
                    onLocationChange = viewModel::onGrowthLocationChange,
                    onRetake = {
                        step = PlantCaptureStep.Camera
                        viewModel.onImageSelected(null)
                    },
                    onSave = viewModel::savePlant
                )
            }
        }
    }
}

@Composable
private fun PlantCameraScreen(
    onBack: () -> Unit,
    onImageSelected: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        onImageSelected(uri)
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) onImageSelected(pendingCameraUri)
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
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
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF123B25), Color(0xFF07160E))
                )
            )
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.16f))
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver", tint = Color.White)
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .clip(RoundedCornerShape(34.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(2.dp, Color.White.copy(alpha = 0.75f), RoundedCornerShape(34.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Spa, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(96.dp))
                Text(
                    text = "Coloca la planta dentro del marco",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(22.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                CameraTip("Buena iluminación")
                CameraTip("Evita fondos muy cargados")
                CameraTip("Enfoca hojas y tallo")
                CameraTip("Mantén la cámara estable")
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.16f))
            ) {
                Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Abrir galería", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = launchCamera,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CameraGreen)
            ) {
                Icon(Icons.Rounded.CameraAlt, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Tomar foto", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CameraTip(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(Icons.Rounded.Check, contentDescription = null, tint = Color(0xFFB9F6CA), modifier = Modifier.size(16.dp))
        Text(text, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PlantAnalysisLoadingScreen(
    imageUri: String?,
    isIdentifying: Boolean,
    isCheckingHealth: Boolean,
    isPreparingPlan: Boolean
) {
    val scanTransition = rememberInfiniteTransition(label = "plant_scan")
    val scanOffset by scanTransition.animateFloat(
        initialValue = -90f,
        targetValue = 90f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scan_offset"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(190.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            CircularProgressIndicator(
                color = CameraGreen,
                strokeWidth = 4.dp,
                modifier = Modifier.size(224.dp)
            )
            Box(
                modifier = Modifier
                    .width(190.dp)
                    .height(3.dp)
                    .graphicsLayer { translationY = scanOffset }
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, CameraGreen.copy(alpha = 0.85f), Color.Transparent)
                        )
                    )
            )
        }
        Spacer(Modifier.height(32.dp))
        Text("Estamos revisando tu planta...", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        LoadingStep("Identificando especie", !isIdentifying)
        LoadingStep("Revisando estado de salud", !isCheckingHealth)
        LoadingStep("Preparando plan de cuidado", isPreparingPlan)
    }
}

@Composable
private fun LoadingStep(text: String, completed: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (completed) CameraGreen else CameraBeige),
            contentAlignment = Alignment.Center
        ) {
            if (completed) Icon(Icons.Rounded.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
        }
        Text(text, color = TextMuted, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PlantReviewScreen(
    uiState: PlantFormUiState,
    onCustomNameChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onRetake: () -> Unit,
    onSave: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CameraBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ReviewHero(uiState = uiState)

        Surface(color = Color.White, shape = RoundedCornerShape(26.dp), shadowElevation = 8.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Confirma tu planta", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                SimpleTextField(
                    label = "Nombre personalizado",
                    value = uiState.customName,
                    placeholder = "Mi Monstera",
                    onValueChange = onCustomNameChange
                )
                Text("Ubicación", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Interior", "Exterior", "Balcón").forEach { option ->
                        val selected = uiState.growthLocation == option
                        Surface(
                            onClick = { onLocationChange(option) },
                            color = if (selected) CameraGreen else CameraBeige,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(modifier = Modifier.height(38.dp), contentAlignment = Alignment.Center) {
                                Text(option, color = if (selected) Color.White else TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        CarePlanSummary(uiState = uiState)

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onRetake,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CameraBeige)
            ) {
                Text("Nueva foto", color = CameraGreenDark, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onSave,
                enabled = !uiState.isSaving,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CameraGreen)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Guardando planta...", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                } else {
                    Text("Guardar planta", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ReviewHero(uiState: PlantFormUiState) {
    Surface(color = Color.White, shape = RoundedCornerShape(30.dp), shadowElevation = 8.dp) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(CameraBeige),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.photoUri != null) {
                    AsyncImage(
                        model = uiState.photoUri,
                        contentDescription = uiState.commonName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Rounded.Spa, contentDescription = null, tint = CameraGreen, modifier = Modifier.size(72.dp))
                }
            }
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = uiState.commonName.ifBlank { "Planta por confirmar" },
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (uiState.scientificName.isNotBlank()) {
                    Text(uiState.scientificName, color = TextMuted, fontSize = 13.sp, fontStyle = FontStyle.Italic)
                }
                Text("Estado: ${uiState.carePlan?.urgency?.displayName ?: "En revisión"}", color = CameraGreenDark, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = uiState.diseaseDiagnosisResult?.diseaseName?.let { "Posible causa: $it" } ?: "No vemos señales graves por ahora.",
                    color = TextMuted,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun CarePlanSummary(uiState: PlantFormUiState) {
    val plan = uiState.carePlan
    Surface(color = Color.White, shape = RoundedCornerShape(26.dp), shadowElevation = 8.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Plan de cuidado", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            if (plan == null) {
                Text("Tu plan básico está listo para guardarse.", color = TextMuted, fontSize = 13.sp)
            } else {
                PlanRow("Riego", plan.wateringRecommendation)
                PlanRow("Luz", plan.lightRecommendation)
                PlanRow("Fertilización", plan.fertilizationRecommendation)
                PlanRow("Poda", plan.pruningRecommendation)
                PlanRow("Próxima revisión", "En ${plan.nextReviewDays} días")
            }
        }
    }
}

@Composable
private fun PlanRow(title: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(title, color = CameraGreenDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(value, color = TextMuted, fontSize = 13.sp, lineHeight = 18.sp)
    }
}

@Composable
private fun SimpleTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Text(label, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        androidx.compose.foundation.text.BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 14.sp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(CameraBeige)
                .padding(horizontal = 16.dp),
            decorationBox = { inner ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                    if (value.isBlank()) Text(placeholder, color = TextMuted, fontSize = 14.sp)
                    inner()
                }
            }
        )
    }
}

private fun createCameraImageUri(context: Context): Uri {
    val imagesDir = File(context.cacheDir, "images").apply { mkdirs() }
    val imageFile = File.createTempFile("plant_", ".jpg", imagesDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
}
