package com.plant.forestcare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plant.forestcare.utils.FakePlant

@Composable
fun PlantCard(
    plant: FakePlant,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFE8F5E9))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = plant.name, fontSize = 20.sp)
            Text(text = plant.scientificName, fontSize = 13.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Ubicación: ${plant.location}", fontSize = 14.sp)
            Text(text = "Próximo riego: ${plant.wateringInfo}", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { plant.healthPercent / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF4CAF50),
                trackColor = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Salud ${plant.healthPercent}% · ${plant.healthStatus}",
                fontSize = 13.sp,
                color = Color(0xFF2E7D32)
            )
        }
    }
}