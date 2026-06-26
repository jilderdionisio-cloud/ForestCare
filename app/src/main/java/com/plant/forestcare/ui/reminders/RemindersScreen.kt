package com.plant.forestcare.ui.reminders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.plant.forestcare.ui.components.AnimatedEntry
import com.plant.forestcare.ui.components.BotanicalHeroArt
import com.plant.forestcare.ui.components.ForestCareBottomBar
import com.plant.forestcare.ui.components.PremiumPlantBackground

private val PlantBackground = Color(0xFFF8FAF7)
private val PlantGreen = Color(0xFF2E7D32)
private val PlantGreenDark = Color(0xFF1B5E20)
private val TextPrimary = Color(0xFF1B1B1B)
private val TextMuted = Color(0xFF616161)
private val AlertOrange = Color(0xFFF9A825)

@Composable
fun RemindersRoute(
    navController: NavController,
    viewModel: RemindersViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RemindersScreen(
        uiState = uiState,
        onComplete = viewModel::completeReminder,
        onPostpone = viewModel::postponeReminder,
        onSkip = viewModel::skipReminder,
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
private fun RemindersScreen(
    uiState: RemindersUiState,
    onComplete: (String) -> Unit,
    onPostpone: (String) -> Unit,
    onSkip: (String) -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            ForestCareBottomBar(activeRoute = Screen.Reminders.route, onNavigate = onNavigate)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            PremiumPlantBackground(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                topColor = Color(0xFFEAF7E6),
                middleColor = Color(0xFFFFF7EB),
                bottomColor = PlantBackground
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        AnimatedEntry {
                            RemindersHero(pendingCount = uiState.reminders.size)
                        }
                    }
                    when {
                        uiState.isLoading -> item {
                            AnimatedEntry(delayMillis = 100) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = PlantGreen)
                                }
                            }
                        }
                        uiState.errorMessage != null -> item {
                            AnimatedEntry(delayMillis = 100) {
                                MessageCard(
                                    title = "No pudimos cargar tus recordatorios",
                                    message = uiState.errorMessage
                                )
                            }
                        }
                        uiState.isEmpty -> item {
                            AnimatedEntry(delayMillis = 100) {
                                MessageCard(
                                    title = "Todo está en calma",
                                    message = "Cuando una planta necesite agua o revisión, aparecerá aquí."
                                )
                            }
                        }
                        else -> items(uiState.reminders, key = { it.id }) { reminder ->
                            AnimatedEntry(delayMillis = 90) {
                                ReminderCard(
                                    reminder = reminder,
                                    onComplete = { onComplete(reminder.id) },
                                    onPostpone = { onPostpone(reminder.id) },
                                    onSkip = { onSkip(reminder.id) }
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
private fun RemindersHero(pendingCount: Int) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 9.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(156.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.White, Color(0xFFEFF8E9))
                    )
                )
                .padding(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 106.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Recordatorios",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (pendingCount == 0) {
                        "Tus plantas no necesitan nada urgente ahora."
                    } else {
                        "$pendingCount cuidado(s) esperando por ti."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
                Row(
                    modifier = Modifier.padding(top = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = PlantGreen, modifier = Modifier.size(17.dp))
                    Text("Tareas de hoy", color = PlantGreenDark, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                }
            }
            BotanicalHeroArt(
                modifier = Modifier.align(Alignment.CenterEnd),
                leafColor = PlantGreen,
                accentColor = Color(0xFF9AD69E)
            )
        }
    }
}

@Composable
private fun ReminderCard(
    reminder: ReminderItemUi,
    onComplete: () -> Unit,
    onPostpone: () -> Unit,
    onSkip: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (reminder.isDue) Color(0xFFFFEEE7) else Color(0xFFE7F6E8)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (reminder.type == "Riego") Icons.Rounded.WaterDrop else Icons.Rounded.Notifications,
                        contentDescription = null,
                        tint = if (reminder.isDue) AlertOrange else PlantGreen
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(reminder.title, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("${reminder.type} · ${reminder.dueText}", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onComplete,
                    colors = ButtonDefaults.buttonColors(containerColor = PlantGreen),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Realizado")
                }
                TextButton(onClick = onPostpone, modifier = Modifier.weight(1f)) {
                    Text("Posponer", color = PlantGreenDark)
                }
                TextButton(onClick = onSkip, modifier = Modifier.weight(1f)) {
                    Text("Omitir", color = AlertOrange)
                }
            }
        }
    }
}

@Composable
private fun MessageCard(title: String, message: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Rounded.Notifications, contentDescription = null, tint = PlantGreenDark, modifier = Modifier.size(34.dp))
            Text(title, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(message, color = TextMuted, style = MaterialTheme.typography.bodySmall)
        }
    }
}
