package com.plant.forestcare.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.plant.forestcare.ui.components.PlantCard
import com.plant.forestcare.utils.Constants

@Composable
fun PlantListScreen(
    onPlantClick: (String) -> Unit,
    onAddPlantClick: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFF6F1EA),
        bottomBar = {
            BottomNavigationBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPlantClick,
                containerColor = Color(0xFF2D4739),
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("+", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header: Avatar, Greeting, Sun Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2EBDC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Good morning, Grower",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D4739)
                    )
                }
                Text("☀️", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFF0F0F0)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔍", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Search your plants...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Horizontal Filters
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip("Todas", true)
                FilterChip("Interior", false)
                FilterChip("Exterior", false)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Plant Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Constants.fakePlants) { plant ->
                    PlantCard(
                        plant = plant,
                        onClick = { onPlantClick(plant.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChip(
    text: String,
    selected: Boolean
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (selected) Color(0xFF2D4739) else Color(0xFFF0F0F0)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            color = if (selected) Color.White else Color(0xFF1B2D24),
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
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
