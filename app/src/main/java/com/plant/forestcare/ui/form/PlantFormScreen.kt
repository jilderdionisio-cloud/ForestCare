package com.plant.forestcare.ui.form

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    viewModel: PlantFormViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PlantFormScreen(
        uiState = uiState,
        onBack = { navController.popBackStack() },
        onCustomNameChange = viewModel::onCustomNameChange,
        onCommonNameChange = viewModel::onCommonNameChange,
        onScientificNameChange = viewModel::onScientificNameChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onGrowthLocationChange = viewModel::onGrowthLocationChange,
        onSunlightExposureChange = viewModel::onSunlightExposureChange,
    onAddTag = { viewModel.onAddTag() },
        onRemoveTag = viewModel::onRemoveTag,
        onSavePlant = viewModel::savePlant,
        onMessageShown = viewModel::onMessageShown,
        onSaved = {
            viewModel.onNavigationHandled()
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Dashboard.route) {
                    inclusive = false
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
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit,
    onSavePlant: () -> Unit,
    onMessageShown: () -> Unit,
    onSaved: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        val message = uiState.errorMessage ?: uiState.successMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            onMessageShown()
            if (uiState.savedSuccessfully) {
                onSaved()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FormBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            PlantFormBottomBar(
                activeRoute = Screen.PlantList.route,
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
                AddPlantHeader(onBack = onBack)
            }
            item {
                PlantPhotoUploadBox(onClick = { /* TODO: Abrir cámara o selector de imagen. */ })
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
                    onAddTag = onAddTag,
                    onRemoveTag = onRemoveTag
                )
            }
            item {
                SavePlantButton(
                    isLoading = uiState.isLoading,
                    onClick = onSavePlant
                )
            }
        }
    }
}

@Composable
private fun AddPlantHeader(onBack: () -> Unit) {
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
            text = "Agregar planta",
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
private fun PlantPhotoUploadBox(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(258.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(PhotoBeige)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
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
                text = "Captura tu planta",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Toca para subir o tomar una foto",
                color = TextMuted,
                fontSize = 10.sp
            )
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
        SectionTitle(icon = Icons.Rounded.Spa, text = "Información de la planta")
        FormCard {
            RoundedInputField(
                label = "Nombre personalizado",
                value = uiState.customName,
                placeholder = "Ej. Monstera de Mateo",
                onValueChange = onCustomNameChange
            )
            RoundedInputField(
                label = "Nombre común",
                value = uiState.commonName,
                placeholder = "Monstera Deliciosa",
                onValueChange = onCommonNameChange
            )
            RoundedInputField(
                label = "Nombre científico",
                value = uiState.scientificName,
                placeholder = "Monstera deliciosa",
                onValueChange = onScientificNameChange
            )
            RoundedInputField(
                label = "Descripción",
                value = uiState.description,
                placeholder = "Notas sobre su crecimiento, historia o cuidados especiales...",
                onValueChange = onDescriptionChange,
                minHeight = 88.dp,
                singleLine = false
            )
        }
    }
}

@Composable
private fun EnvironmentSection(
    uiState: PlantFormUiState,
    onGrowthLocationChange: (String) -> Unit,
    onSunlightExposureChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(icon = Icons.Rounded.Yard, text = "Ambiente")
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
        listOf("Interior", "Exterior").forEach { option ->
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
        SunlightOption("Baja", Icons.Rounded.KeyboardArrowDown, selected == "Baja", Modifier.weight(1f), onSelected)
        SunlightOption("Media", Icons.Rounded.WbSunny, selected == "Media", Modifier.weight(1f), onSelected)
        SunlightOption("Alta", Icons.Rounded.WbSunny, selected == "Alta", Modifier.weight(1f), onSelected)
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
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
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
            text = if (isLoading) "Guardando..." else "Guardar planta",
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
            onAddTag = {},
            onRemoveTag = {},
            onSavePlant = {},
            onMessageShown = {},
            onSaved = {},
            onNavigate = {}
        )
    }
}
