package com.plant.forestcare.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.plant.forestcare.navigation.Screen
import com.plant.forestcare.ui.components.ForestCareBottomBar

private val PlantBackground = Color(0xFFF8FAF7)
private val PlantGreen = Color(0xFF2E7D32)
private val PlantGreenDark = Color(0xFF1B5E20)
private val TextPrimary = Color(0xFF1B1B1B)
private val TextMuted = Color(0xFF616161)

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
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFE8F5E9), PlantBackground, Color.White)
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Person, contentDescription = null, tint = PlantGreenDark)
                        }
                        Column {
                            Text(uiState.title, style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text(uiState.subtitle, color = TextMuted)
                        }
                    }
                }
                item {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(26.dp),
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Rounded.Settings, contentDescription = null, tint = PlantGreen)
                            Text(uiState.preferencesTitle, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text(uiState.preferencesDescription, color = TextMuted)
                        }
                    }
                }
            }
        }
    }
}
