package com.plant.forestcare.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        containerColor = Color(0xFFF5F0E8),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPlantClick,
                containerColor = Color(0xFF4CAF50)
            ) {
                Text("+", color = Color.White, fontSize = 26.sp)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "Mis plantas",
                fontSize = 30.sp,
                color = Color(0xFF2E7D32)
            )

            Text(
                text = "Cuida tu jardín digital",
                fontSize = 15.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Color.White
            ) {
                Text(
                    text = "Buscar planta...",
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                FilterChip("Todas", true)
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip("Interior", false)
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip("Suculentas", false)
            }

            Spacer(modifier = Modifier.height(18.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
        shape = RoundedCornerShape(24.dp),
        color = if (selected) Color(0xFF2E7D32) else Color.White
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
            color = if (selected) Color.White else Color.Black,
            fontSize = 13.sp
        )
    }
}