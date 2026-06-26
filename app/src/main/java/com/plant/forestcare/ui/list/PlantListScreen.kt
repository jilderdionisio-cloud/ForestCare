package com.plant.forestcare.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import com.plant.forestcare.navigation.Screen
import com.plant.forestcare.ui.components.AnimatedEntry
import com.plant.forestcare.ui.components.ForestCareBottomBar
import com.plant.forestcare.ui.components.PremiumPlantBackground

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
    viewModel: PlantListViewModel = viewModel()
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
@OptIn(ExperimentalFoundationApi::class)
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
    var fabVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fabVisible = true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        floatingActionButton = {
            AnimatedVisibility(
                visible = fabVisible,
                enter = scaleIn(
                    animationSpec = tween(durationMillis = 260),
                    initialScale = 0.82f
                ) + fadeIn(animationSpec = tween(durationMillis = 220))
            ) {
                FloatingActionButton(
                    onClick = onAddPlantClick,
                    containerColor = PlantGreenStrong,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(54.dp)
                        .offset(y = 10.dp)
                        .shadow(10.dp, CircleShape, ambientColor = PlantGreen.copy(alpha = 0.28f))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Agregar nueva planta",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        },
        bottomBar = {
            ForestCareBottomBar(
                activeRoute = Screen.PlantList.route,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        PremiumPlantBackground(
            modifier = Modifier
                .fillMaxSize()
                .background(PlantBackground),
            topColor = Color(0xFFE8F5E9),
            middleColor = Color(0xFFFAF4E8),
            bottomColor = PlantBackground
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(start = 16.dp, top = 18.dp, end = 16.dp, bottom = 18.dp),
                verticalItemSpacing = 14.dp,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    PlantListHeader(totalPlants = uiState.plants.size)
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    SearchSummaryCard(
                        query = uiState.searchQuery,
                        onQueryChange = onSearchQueryChange
                    )
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    PlantFilterRow(
                        selectedFilter = uiState.selectedFilter,
                        onFilterSelected = onFilterSelected
                    )
                }

                when {
                    uiState.isLoading -> {
                        item(span = StaggeredGridItemSpan.FullLine) { LoadingState() }
                    }
                    uiState.errorMessage != null -> {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            MessageState(
                                title = "No pudimos cargar tus plantas",
                                message = uiState.errorMessage,
                                actionText = "Reintentar",
                                onActionClick = onRetry
                            )
                        }
                    }
                    uiState.isEmpty -> {
                        item(span = StaggeredGridItemSpan.FullLine) { EmptyPlantsState() }
                    }
                    else -> {
                        items(uiState.plants, key = { it.id }) { plant ->
                            AnimatedEntry(delayMillis = 80) {
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
    }
}

@Composable
private fun PlantListHeader(totalPlants: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFBFD8B5), Color(0xFF7FB678))
                    )
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Spa, contentDescription = null, tint = PlantGreenDark, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Good morning, Grower",
                color = PlantGreenDark,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$totalPlants plantas en tu jardín",
                color = TextMuted,
                fontSize = 11.sp,
                lineHeight = 13.sp
            )
        }
        Surface(
            color = Color.White.copy(alpha = 0.74f),
            shape = CircleShape,
            shadowElevation = 3.dp
        ) {
            Icon(
                Icons.Rounded.Spa,
                contentDescription = null,
                tint = PlantGreen,
                modifier = Modifier
                    .padding(9.dp)
                    .size(18.dp)
            )
        }
    }
}

@Composable
private fun SearchSummaryCard(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Surface(
        color = Color(0xFFF0ECE4),
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Rounded.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                modifier = Modifier.weight(1f),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (query.isBlank()) {
                            Text("Search your plants...", color = TextMuted, fontSize = 13.sp)
                        }
                        innerTextField()
                    }
                }
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
            val compactLabel = when (filter) {
                PlantListFilter.All -> "Todas"
                PlantListFilter.Healthy -> "Saludables"
                PlantListFilter.Attention -> "Revisar"
                PlantListFilter.Urgent -> "Urgentes"
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (selected) PlantGreen else Color(0xFFF0ECE4))
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = compactLabel,
                    color = if (selected) Color.White else TextMuted,
                    fontSize = 10.sp,
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
    val imageHeight = if (plant.id.hashCode().mod(3) == 0) 190.dp else 132.dp
    val healthScore = plant.healthScore()
    val healthColor = when {
        healthScore < 45 -> Color(0xFFD32F2F)
        healthScore < 72 -> UrgentOrange
        else -> PlantGreen
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(22.dp), ambientColor = PlantGreen.copy(alpha = 0.10f))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(bottom = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
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
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.88f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = plant.location.ifBlank { "Interior" }.uppercase(),
                    color = PlantGreen,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1
                )
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 11.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = plant.name,
                color = TextPrimary,
                fontSize = 16.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            PlantCareHint(plant = plant, healthColor = healthColor)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFECE8DE))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(healthScore / 100f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(healthColor)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Health", color = TextMuted, fontSize = 11.sp, modifier = Modifier.weight(1f))
                Text("$healthScore%", color = healthColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PlantCareHint(plant: PlantListItemUi, healthColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .clip(CircleShape)
                .background(healthColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(healthColor)
            )
        }
        val message = when {
            plant.hasDisease -> plant.diagnosis.ifBlank { "Necesita revisión" }
            plant.nextCareText.equals("Hoy", ignoreCase = true) -> "Necesita cuidado hoy"
            else -> plant.nextCareText.ifBlank { "Cuidado estable" }
        }
        Text(
            text = message,
            color = TextMuted,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
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

@Composable
private fun PlantListBottomBar(
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

private fun String.toFriendlyHealth(): String {
    return when {
        contains("urgente", ignoreCase = true) -> "Atención urgente"
        contains("atención", ignoreCase = true) || contains("atencion", ignoreCase = true) -> "Revisar pronto"
        contains("salud", ignoreCase = true) -> "Saludable"
        else -> this
    }
}

private fun PlantListItemUi.healthScore(): Int {
    return when {
        hasDisease -> 32
        riskLevel?.contains("alto", ignoreCase = true) == true -> 45
        riskLevel?.contains("medio", ignoreCase = true) == true -> 72
        healthStatus.contains("urgente", ignoreCase = true) -> 38
        healthStatus.contains("atención", ignoreCase = true) || healthStatus.contains("atencion", ignoreCase = true) -> 68
        healthStatus.contains("salud", ignoreCase = true) -> 92
        else -> 85
    }
}
