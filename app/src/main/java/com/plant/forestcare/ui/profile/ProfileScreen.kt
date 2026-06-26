package com.plant.forestcare.ui.profile

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.plant.forestcare.navigation.Screen
import com.plant.forestcare.ui.components.AnimatedEntry
import com.plant.forestcare.ui.components.ForestCareBottomBar
import com.plant.forestcare.ui.components.PremiumPlantBackground
import com.plant.forestcare.ui.theme.ForestCareTheme

private val ProfileBackground = Color(0xFFF5EFE5)
private val ProfileGreen = Color(0xFF007F25)
private val ProfileGreenSoft = Color(0xFFE8F6E8)
private val ProfileText = Color(0xFF171B15)
private val ProfileMuted = Color(0xFF646D61)
private val ProfileDivider = Color(0xFFF0F0EA)
private val LogoutRed = Color(0xFFE44C4C)

@Composable
fun ProfileRoute(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        uiState = uiState,
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
private fun ProfileScreen(
    uiState: ProfileUiState,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            ForestCareBottomBar(activeRoute = Screen.Profile.route, onNavigate = onNavigate)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            PremiumPlantBackground(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                topColor = Color(0xFFFDF8EF),
                middleColor = ProfileBackground,
                bottomColor = Color(0xFFF9F4EB)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 22.dp, top = 26.dp, end = 22.dp, bottom = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        AnimatedEntry {
                            ProfileTopBar(title = uiState.title)
                        }
                    }
                    item {
                        AnimatedEntry(delayMillis = 80) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(Modifier.height(34.dp))
                                ProfileAvatar()
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = uiState.userName,
                                    color = ProfileText,
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = uiState.email,
                                    color = ProfileMuted,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                    }
                    item {
                        AnimatedEntry(delayMillis = 150) {
                            Column {
                                Spacer(Modifier.height(34.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    MetricCard(
                                        value = uiState.activePlants.toString(),
                                        label = "Active Plants",
                                        modifier = Modifier.weight(1f)
                                    )
                                    MetricCard(
                                        value = "${uiState.healthScore}%",
                                        label = "Health Score",
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        AnimatedEntry(delayMillis = 220) {
                            Column {
                                Spacer(Modifier.height(32.dp))
                                ProfileOptionsCard()
                            }
                        }
                    }
                    item {
                        AnimatedEntry(delayMillis = 300) {
                            Column {
                                Spacer(Modifier.height(40.dp))
                                LeafWatermark()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileTopBar(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = ProfileGreen,
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = Icons.Rounded.PersonAdd,
            contentDescription = "Invitar usuario",
            tint = ProfileGreen,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun ProfileAvatar() {
    Box(contentAlignment = Alignment.BottomEnd) {
        Surface(
            modifier = Modifier
                .size(88.dp)
                .shadow(10.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.18f))
                .border(4.dp, Color.White, CircleShape),
            shape = CircleShape,
            color = Color(0xFF123D2D)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFF2D6D4A), Color(0xFF0E2F24)),
                        center = Offset(size.width * 0.5f, size.height * 0.28f),
                        radius = size.width * 0.74f
                    )
                )
                repeat(9) { index ->
                    val x = size.width * (0.12f + index * 0.1f)
                    val leaf = Path().apply {
                        moveTo(x, size.height * 0.86f)
                        cubicTo(x - 20f, size.height * 0.54f, x + 2f, size.height * 0.26f, x + 34f, size.height * 0.18f)
                        cubicTo(x + 42f, size.height * 0.48f, x + 26f, size.height * 0.78f, x, size.height * 0.86f)
                        close()
                    }
                    drawPath(leaf, Color(0xFF6AA36E).copy(alpha = 0.62f))
                }
                drawCircle(Color(0xFFE8C4A3), radius = size.width * 0.15f, center = Offset(size.width * 0.5f, size.height * 0.41f))
                drawRoundRect(
                    color = Color(0xFFF8E8D6),
                    topLeft = Offset(size.width * 0.34f, size.height * 0.58f),
                    size = androidx.compose.ui.geometry.Size(size.width * 0.32f, size.height * 0.28f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
                )
                drawCircle(Color(0xFF7B4D35), radius = size.width * 0.17f, center = Offset(size.width * 0.5f, size.height * 0.34f))
            }
        }
        Surface(
            modifier = Modifier
                .size(30.dp)
                .border(3.dp, Color.White, CircleShape),
            shape = CircleShape,
            color = ProfileGreen
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Editar foto",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(78.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                color = ProfileGreen,
                fontSize = 22.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = ProfileText,
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProfileOptionsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 18.dp)
        ) {
            ProfileMenuRow(
                icon = Icons.Rounded.Edit,
                label = "Editar perfil",
                iconBackground = ProfileGreenSoft,
                iconTint = ProfileGreen
            )
            MenuDivider()
            ProfileMenuRow(
                icon = Icons.Rounded.Settings,
                label = "Configuración",
                iconBackground = Color(0xFFE7F8E1),
                iconTint = ProfileGreen
            )
            MenuDivider()
            ProfileMenuRow(
                icon = Icons.Rounded.Notifications,
                label = "Notificaciones",
                iconBackground = Color(0xFFE7F8E1),
                iconTint = ProfileGreen,
                showNotificationDot = true
            )
            MenuDivider()
            ProfileMenuRow(
                icon = Icons.AutoMirrored.Rounded.HelpOutline,
                label = "Ayuda",
                iconBackground = Color(0xFFF5F5F0),
                iconTint = Color(0xFF93A091)
            )
            MenuDivider()
            ProfileMenuRow(
                icon = Icons.AutoMirrored.Rounded.Logout,
                label = "Cerrar sesión",
                iconBackground = Color(0xFFFFE6E6),
                iconTint = LogoutRed,
                labelColor = LogoutRed,
                showChevron = false
            )
        }
    }
}

@Composable
private fun ProfileMenuRow(
    icon: ImageVector,
    label: String,
    iconBackground: Color,
    iconTint: Color,
    labelColor: Color = ProfileText,
    showChevron: Boolean = true,
    showNotificationDot: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            color = iconBackground
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
        Spacer(Modifier.width(14.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = labelColor,
            fontSize = 13.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = if (labelColor == LogoutRed) FontWeight.Medium else FontWeight.Normal
        )
        if (showNotificationDot) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .background(Color(0xFFD1222A), CircleShape)
            )
            Spacer(Modifier.width(14.dp))
        }
        if (showChevron) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF9BA59A),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun MenuDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 46.dp),
        color = ProfileDivider,
        thickness = 1.dp
    )
}

@Composable
private fun LeafWatermark() {
    Canvas(modifier = Modifier.size(42.dp)) {
        val stroke = Color(0xFFA6D6A8)
        val leaf = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.88f)
            cubicTo(size.width * 0.08f, size.height * 0.52f, size.width * 0.28f, size.height * 0.08f, size.width * 0.86f, size.height * 0.08f)
            cubicTo(size.width * 0.9f, size.height * 0.58f, size.width * 0.72f, size.height * 0.84f, size.width * 0.5f, size.height * 0.88f)
        }
        drawPath(leaf, stroke.copy(alpha = 0.65f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f))
        drawLine(
            color = stroke.copy(alpha = 0.65f),
            start = Offset(size.width * 0.52f, size.height * 0.82f),
            end = Offset(size.width * 0.76f, size.height * 0.24f),
            strokeWidth = 4f
        )
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 812)
@Composable
private fun ProfileScreenPreview() {
    ForestCareTheme(dynamicColor = false) {
        ProfileScreen(
            uiState = ProfileUiState(),
            onNavigate = {}
        )
    }
}
