package com.plant.forestcare.data

import android.util.Log
import com.plant.forestcare.core.constants.AppConstants
import com.plant.forestcare.data.remote.RetrofitInstance
import com.plant.forestcare.data.remote.api.PlantIdApiService
import com.plant.forestcare.data.remote.dto.PlantIdRequestDto
import com.plant.forestcare.data.remote.dto.PlantSuggestionDto
import com.plant.forestcare.domain.model.PlantIdentificationResult

class PlantIdRepository(
    private val apiService: PlantIdApiService = RetrofitInstance.plantIdApiService
) {
    suspend fun identifyPlant(imageBase64: String): PlantIdentificationResult {
        Log.d(TAG, "Enviando imagen a Plant.id")
        val response = apiService.identifyPlant(
            apiKey = AppConstants.PLANT_ID_API_KEY,
            request = PlantIdRequestDto(images = listOf(imageBase64))
        )
        Log.d(TAG, "HTTP code: ${response.code()}")
        Log.d(TAG, "Response: ${response.body()}")

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string().orEmpty()
            Log.e(TAG, "Error body: $errorBody")
            throw PlantIdApiException(
                code = response.code(),
                errorBody = errorBody,
                message = when (response.code()) {
                    400 -> "Imagen inválida o solicitud incorrecta."
                    401, 403 -> "API key inválida o sin permisos."
                    429 -> "Límite de consultas alcanzado."
                    in 500..599 -> "Error del servidor de identificación."
                    else -> "No se pudo identificar la planta."
                }
            )
        }

        val suggestion = response.body()
            ?.result
            ?.classification
            ?.suggestions
            ?.maxByOrNull { it.probability ?: 0.0 }
            ?: throw PlantIdApiException(
                code = response.code(),
                errorBody = "",
                message = "No se pudo identificar la planta."
            )

        return suggestion.toDomain()
    }

    private fun PlantSuggestionDto.toDomain(): PlantIdentificationResult {
        val scientific = name?.takeIf { it.isNotBlank() } ?: "Nombre científico no disponible"
        val common = details?.commonNames?.firstOrNull { it.isNotBlank() } ?: scientific
        val confidence = (probability ?: 0.0) * 100
        val description = details?.description?.value?.takeIf { it.isNotBlank() }

        return PlantIdentificationResult(
            commonName = common,
            scientificName = scientific,
            confidence = confidence,
            imageUrl = details?.image?.value
                ?: similarImages?.firstOrNull { !it.url.isNullOrBlank() }?.url,
            description = description
        )
    }

    companion object {
        private const val TAG = "PlantIdAPI"
    }
}

class PlantIdApiException(
    val code: Int,
    val errorBody: String,
    override val message: String
) : Exception(message)
