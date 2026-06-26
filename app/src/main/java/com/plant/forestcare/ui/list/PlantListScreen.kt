package com.plant.forestcare.ui.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.plant.forestcare.navigation.Screen
import com.plant.forestcare.ui.components.ForestCareBottomBar

private val PlantBackground = Color(0xFFF8FAF7)
private val PlantGreen = Color(0xFF2E7D32)
private val PlantGreenDark = Color(0xFF1B5E20)
private val PlantGreenStrong = Color(0xFF2E7D32)
private val TextPrimary = Color(0xFF1B1B1B)
private val TextMuted = Color(0xFF616161)
private val WarmBeige = Color(0xFFF3EAD9)
private val UrgentOrange = Color(0xFFF9A825)

@Composable
fun PlantListRoute(
    onPlantClick: (String) -> Unit,
    onAddPlantClick: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: PlantListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PlantListScreen(
        uiState = uiState,
        onPlantClick = onPlantClick,
        onAddPlantClick = onAddPlantClick,
        onRetry = viewModel::retry,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onFilterSelected = viewModel::onFilterSelected,
        onNavigate = onNavigate
    )
}

@Composable
fun PlantListScreen(
    uiState: PlantListUiState,
    onPlantClick: (String) -> Unit,
    onAddPlantClick: () -> Unit,
    onRetry: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onFilterSelected: (PlantListFilter) -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFab by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showFab = true
    }

    Scaffold(
        modifier = modifier,
        containerColor = PlantBackground,
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn(tween(600)) + scaleIn(tween(600, delayMillis = 100))
            ) {
                FloatingActionButton(
                    onClick = onAddPlantClick,
                    containerColor = PlantGreen,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.shadow(12.dp, CircleShape, ambientColor = PlantGreen.copy(alpha = 0.4f))
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Añadir planta")
                }
            }
        },
        bottomBar = {
            ForestCareBottomBar(
                activeRoute = Screen.PlantList.route,
                onNavigate = onNavigate
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                PlantListHeader(
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = onSearchQueryChange
                )
            }

            item {
                SearchSummaryCard(
                    query = uiState.searchQuery,
                    count = uiState.plants.size,
                    isVisible = uiState.searchQuery.isNotBlank()
                )
            }

            item {
                PlantListSectionHeader()
            }

            item {
                PlantFilterRow(
                    selectedFilter = uiState.selectedFilter,
                    onFilterSelected = onFilterSelected
                )
            }

            when {
                uiState.isLoading -> {
                    item { LoadingState() }
                }
                uiState.errorMessage != null -> {
                    item {
                        MessageState(
                            title = "Ups, algo salió mal",
                            message = uiState.errorMessage,
                            actionText = "Reintentar",
                            onActionClick = onRetry
                        )
                    }
                }
                uiState.isEmpty -> {
                    item { EmptyPlantsState() }
                }
                else -> {
                    items(uiState.plants, key = { it.id }) { plant ->
                        PlantListCard(
                            plant = plant,
                            onClick = { onPlantClick(plant.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlantListHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Mi Jardín",
            color = TextPrimary,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 36.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Gestiona y cuida tus plantas favoritas",
            color = TextMuted,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        SearchBar(query = searchQuery, onQueryChange = onSearchQueryChange)
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp), ambientColor = Color.Black.copy(alpha = 0.05f)),
        color = Color.White,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text("Buscar planta...", color = TextMuted.copy(alpha = 0.6f), fontSize = 15.sp)
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 15.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SearchSummaryCard(
    query: String,
    count: Int,
    isVisible: Boolean
) {
    AnimatedVisibility(visible = isVisible) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PlantGreen.copy(alpha = 0.08f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Encontradas $count plantas para \"$query\"",
                modifier = Modifier.padding(12.dp),
                color = PlantGreenDark,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PlantFilterRow(
    selectedFilter: PlantListFilter,
    onFilterSelected: (PlantListFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlantListFilter.entries.forEach { filter ->
            val selected = filter == selectedFilter
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (selected) PlantGreen else Color.White)
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = filter.label,
                    color = if (selected) Color.White else TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PlantListSectionHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Tus plantas",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PlantListCard(
    plant: PlantListItemUi,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(154.dp)
            .shadow(12.dp, RoundedCornerShape(30.dp), ambientColor = PlantGreen.copy(alpha = 0.10f))
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color.White, Color(0xFFF9FCF7))
                )
            )
            .clickable(onClick = onClick)
            .padding(start = 14.dp, top = 14.dp, end = 14.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            StatusBadge(text = plant.location.uppercase())
            if (plant.hasDisease) {
                StatusBadge(text = "REVISAR")
            }
            plant.riskLevel?.takeIf { it.isNotBlank() }?.let { risk ->
                StatusBadge(text = "RIESGO ${risk.uppercase()}")
            }
            Column {
                Text(
                    text = plant.name,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val scientificName = plant.scientificName.ifBlank { "Nombre científico pendiente" }
                Text(
                    text = scientificName,
                    color = TextMuted,
                    fontStyle = FontStyle.Italic,
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlantMetric(title = "Estado", value = plant.healthStatus.toFriendlyHealth(), valueColor = PlantGreenDark)
                PlantMetric(
                    title = "Próximo",
                    value = plant.nextCareText,
                    valueColor = if (plant.nextCareText.equals("Hoy", ignoreCase = true)) UrgentOrange else TextPrimary
                )
            }
            Text(
                text = "${plant.diagnosis} · Revisión ${plant.nextReviewText}",
                color = TextMuted,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        PlantPhotoThumb(plant = plant)
    }
}

@Composable
private fun StatusBadge(text: String) {
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
            fontSize = 8.sp,
            lineHeight = 9.sp,
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
        Text(text = title, color = TextMuted, fontSize = 9.sp, lineHeight = 10.sp)
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
private fun PlantPhotoThumb(plant: PlantListItemUi) {
    Box(
        modifier = Modifier
            .width(124.dp)
            .height(126.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(WarmBeige),
        contentAlignment = Alignment.Center
    ) {
        if (plant.photoUri != null) {
            AsyncImage(
                model = plant.photoUri,
                contentDescription = plant.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            PlantIllustration(seed = plant.id)
        }
    }
}

@Composable
private fun PlantIllustration(seed: String) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.White.copy(alpha = 0.55f),
            radius = size.minDimension * 0.34f,
            center = Offset(size.width * 0.55f, size.height * 0.48f)
        )
        drawRoundRect(
            color = Color(0xFFD4B894),
            topLeft = Offset(size.width * 0.35f, size.height * 0.66f),
            size = Size(size.width * 0.32f, size.height * 0.11f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
            color = Color(0xFFE7D8C3),
            topLeft = Offset(size.width * 0.39f, size.height * 0.74f),
            size = Size(size.width * 0.24f, size.height * 0.16f),
            cornerRadius = CornerRadius(12f, 12f)
        )
        val leafColor = if (seed.length % 2 == 0) Color(0xFF2F7D39) else Color(0xFF54A954)
        val path = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.68f)
            cubicTo(size.width * 0.22f, size.height * 0.24f, size.width * 0.32f, size.height * 0.14f, size.width * 0.45f, size.height * 0.34f)
            cubicTo(size.width * 0.72f, size.height * 0.16f, size.width * 0.78f, size.height * 0.42f, size.width * 0.5f, size.height * 0.68f)
        }
        drawPath(path, leafColor)
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = PlantGreen)
    }
}

@Composable
private fun MessageState(
    title: String,
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(message, color = TextMuted, fontSize = 12.sp, lineHeight = 16.sp)
        if (actionText != null && onActionClick != null) {
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(actionText, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EmptyPlantsState() {
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
        Text("Tu jardín está vacío", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Agrega una planta para ver sus cuidados aquí.",
            color = TextMuted,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

private fun String.toFriendlyHealth(): String {
    return when {
        contains("urgente", ignoreCase = true) -> "Atención urgente"
        contains("atención", ignoreCase = true) || contains("atencion", ignoreCase = true) -> "Revisar pronto"
        contains("salud", ignoreCase = true) -> "Saludable"
        else -> this
    }
}
