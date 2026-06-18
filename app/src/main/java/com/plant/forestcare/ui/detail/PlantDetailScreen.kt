package com.plant.forestcare.ui.detail

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
            }
        }
    }
}

@Composable
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
            }
        }
    }
}

@Composable
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
