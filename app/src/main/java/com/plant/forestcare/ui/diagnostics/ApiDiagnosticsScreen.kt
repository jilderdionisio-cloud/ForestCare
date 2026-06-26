package com.plant.forestcare.ui.diagnostics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.plant.forestcare.data.diagnostics.ApiDiagnosticResult
import com.plant.forestcare.data.diagnostics.ApiDiagnosticStatus

@Composable
fun ApiDiagnosticsRoute(
    viewModel: ApiDiagnosticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ApiDiagnosticsScreen(
        uiState = uiState,
        onImageBase64Change = viewModel::onImageBase64Change,
        onVerifyClick = viewModel::verifyAllApis
    )
}

@Composable
private fun ApiDiagnosticsScreen(
    uiState: ApiDiagnosticsUiState,
    onImageBase64Change: (String) -> Unit,
    onVerifyClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7F2)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "API Diagnostics",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            OutlinedTextField(
                value = uiState.imageBase64,
                onValueChange = onImageBase64Change,
                label = { Text("Imagen Base64") },
                minLines = 4,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(
                onClick = onVerifyClick,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.height(18.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.padding(horizontal = 4.dp))
                }
                Text("Ejecutar verifyAllApis()")
            }
        }
        uiState.errorMessage?.let { message ->
            item {
                Text(text = message, color = Color(0xFFB3261E))
            }
        }
        item {
            DiagnosticCard(title = "API Identificación", result = uiState.plantIdentification)
        }
        item {
            DiagnosticCard(title = "API Enfermedad", result = uiState.diseaseDetection)
        }
        item {
            DiagnosticCard(title = "Gemini", result = uiState.gemini)
        }
    }
}

@Composable
private fun DiagnosticCard(
    title: String,
    result: ApiDiagnosticResult?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(
                    text = result?.status?.label ?: "Sin ejecutar",
                    color = result?.status.statusColor(),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text("HTTP: ${result?.httpCode ?: "-"}")
            Text("Tiempo: ${result?.elapsedMs ?: 0} ms")
            result?.modelUsed?.let { model ->
                Text("Modelo: $model")
            }
            if (result?.fallbackUsed == true) {
                Text("Fallback: usado", color = Color(0xFF8A5A00), fontWeight = FontWeight.SemiBold)
            }
            Text(
                text = "URL: ${result?.url ?: "-"}",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (!result?.missingFields.isNullOrEmpty()) {
                Text("Campos faltantes: ${result?.missingFields?.joinToString()}", color = Color(0xFF8A5A00))
            }
            result?.error?.let { error ->
                Text("Error: $error", color = Color(0xFFB3261E))
            }
            result?.responsePreview?.takeIf { it.isNotBlank() }?.let { preview ->
                Text(
                    text = "Respuesta: $preview",
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun ApiDiagnosticStatus?.statusColor(): Color {
    return when (this) {
        ApiDiagnosticStatus.Working -> Color(0xFF2E7D32)
        ApiDiagnosticStatus.IncompleteResponse -> Color(0xFF8A5A00)
        ApiDiagnosticStatus.Error -> Color(0xFFB3261E)
        null -> Color(0xFF6F7568)
    }
}
