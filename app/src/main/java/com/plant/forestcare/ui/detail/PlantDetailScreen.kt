package com.plant.forestcare.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plant.forestcare.utils.Constants

@Composable
fun PlantDetailScreen(
    plantId: String,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    val plant = Constants.fakePlants.find { it.id == plantId }

    Scaffold(
        containerColor = Color(0xFFF5F0E8)
    ) { padding ->

        if (plant == null) {
            Text(
                text = "Planta no encontrada",
                modifier = Modifier.padding(24.dp)
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp)
        ) {
            TextButton(onClick = onBackClick) {
                Text("← Volver", color = Color(0xFF2E7D32))
            }

            Text(
                text = plant.name,
                fontSize = 30.sp,
                color = Color(0xFF2E7D32)
            )

            Text(
                text = plant.scientificName,
                fontSize = 15.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(32.dp)
                    )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = plant.description, fontSize = 15.sp)

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareCard("Riego", plant.wateringInfo, Modifier.weight(1f))
                CareCard("Luz", plant.lightInfo, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CareCard("Abono", plant.fertilizerInfo, Modifier.weight(1f))
                CareCard("Poda", plant.pruningInfo, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEditClick(plant.id) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Editar planta")
            }
        }
    }
}

@Composable
private fun CareCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 18.sp, color = Color(0xFF2E7D32))
        }
    }
}