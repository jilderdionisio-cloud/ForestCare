package com.plant.forestcare.data.remote

import android.util.Log
import com.google.gson.JsonObject
import com.plant.forestcare.core.config.ApiConfig
import com.plant.forestcare.data.PlantIdApiException
import com.plant.forestcare.data.remote.api.PlantDiseaseApiService
import com.plant.forestcare.data.remote.api.PlantIdentificationApiService
import com.plant.forestcare.data.remote.dto.PlantDiseaseRequestDto
import com.plant.forestcare.data.remote.dto.PlantIdRequestDto
import com.plant.forestcare.data.remote.dto.PlantSuggestionDto
import com.plant.forestcare.domain.model.DiseaseDiagnosisResult
import com.plant.forestcare.domain.model.PlantApiTestResult
import com.plant.forestcare.domain.model.PlantApisConnectionTestResult
import com.plant.forestcare.domain.model.PlantIdentificationResult
import java.util.concurrent.TimeUnit

class PlantRemoteDataSource(
    private val identificationApi: PlantIdentificationApiService = RetrofitInstance.plantIdentificationApiService,
    private val diseaseApi: PlantDiseaseApiService = RetrofitInstance.plantDiseaseApiService
) {
    private val healthQueryValue: String = ApiConfig.PLANT_HEALTH_MODE.substringAfter("=")

    suspend fun identifyPlant(imageBase64: String): PlantIdentificationResult {
        val normalizedImage = imageBase64.normalizedBase64()
        logImagePayload("plant.id identification", normalizedImage)
        val startedAt = System.nanoTime()
        val response = identificationApi.identifyPlant(
            apiKey = ApiConfig.PLANT_ID_API_KEY,
            request = PlantIdRequestDto(images = listOf(normalizedImage))
        )
        val elapsedMs = startedAt.elapsedMs()
        Log.d(TAG, "[FLOW][PLANT_ID] HTTP=${response.code()} elapsedMs=$elapsedMs success=${response.isSuccessful}")
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string().orEmpty()
            Log.e(TAG, "[FLOW][PLANT_ID] Error Plant.id: code=${response.code()}, elapsedMs=$elapsedMs, errorBody=${errorBody.preview()}")
            throw PlantIdApiException(
                code = response.code(),
                errorBody = errorBody,
                message = response.code().toApiMessage(
                    defaultMessage = "No se pudo identificar la planta."
                )
            )
        }

        val suggestion = response.body()
            ?.result
            ?.classification
            ?.suggestions
            ?.maxByOrNull { it.probability ?: 0.0 }
            ?: throw PlantIdApiException(response.code(), "", "No se pudo identificar la planta.")

        val result = suggestion.toDomain()
        Log.d(TAG, "[FLOW][PLANT_ID] Respuesta usada: commonName=${result.commonName}, confidence=${result.confidence}, elapsedMs=$elapsedMs")
        return result
    }

    suspend fun diagnoseDisease(
        imageBase64: String,
        identification: PlantIdentificationResult? = null
    ): DiseaseDiagnosisResult {
        val normalizedImage = imageBase64.normalizedBase64()
        Log.d(TAG, "[FLOW][DISEASE] Llamando Kindwise crop.health en flujo real. hasIdentification=${identification != null}")
        return runCatching { diagnoseCropHealth(normalizedImage) }
            .recoverCatching { error ->
                Log.w(
                    TAG,
                    "[FLOW][DISEASE] Kindwise falló, usando fallback plant.health: ${error.message}"
                )
                diagnosePlantHealth(normalizedImage)
            }
            .getOrThrow()
    }

    suspend fun testPlantApisConnection(imageBase64: String): PlantApisConnectionTestResult {
        val normalizedImage = imageBase64.normalizedBase64()
        logImagePayload("testPlantApisConnection", normalizedImage)
        return PlantApisConnectionTestResult(
            identification = testIdentificationApi(normalizedImage),
            cropHealth = testCropHealthApi(normalizedImage),
            plantHealth = testPlantHealthApi(normalizedImage)
        )
    }

    private suspend fun diagnoseCropHealth(imageBase64: String): DiseaseDiagnosisResult {
        logImagePayload("crop.health", imageBase64)
        val startedAt = System.nanoTime()
        val response = diseaseApi.diagnoseDisease(
            apiKey = ApiConfig.PLANT_DISEASE_API_KEY,
            request = PlantDiseaseRequestDto(images = listOf(imageBase64))
        )
        val elapsedMs = startedAt.elapsedMs()
        Log.d(TAG, "[FLOW][DISEASE] Kindwise HTTP=${response.code()} elapsedMs=$elapsedMs success=${response.isSuccessful}")
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string().orEmpty()
            Log.e(TAG, "[FLOW][DISEASE] Kindwise error: code=${response.code()}, elapsedMs=$elapsedMs, errorBody=${errorBody.preview()}")
            throw PlantIdApiException(
                code = response.code(),
                errorBody = errorBody,
                message = response.code().toApiMessage(
                    defaultMessage = "crop.health no pudo analizar esta imagen. Puede estar fuera de los cultivos soportados."
                )
            )
        }

        val body = response.body()
        Log.d(TAG, "[FLOW][DISEASE] Kindwise respuesta usada: body=${body.toString().preview()}")
        return body.toDiseaseDiagnosisResult()
    }

    private suspend fun diagnosePlantHealth(imageBase64: String): DiseaseDiagnosisResult {
        logImagePayload("plant.health", imageBase64)
        val startedAt = System.nanoTime()
        val response = identificationApi.identifyPlantWithHealth(
            apiKey = ApiConfig.PLANT_ID_API_KEY,
            health = healthQueryValue,
            request = PlantIdRequestDto(images = listOf(imageBase64))
        )
        val elapsedMs = startedAt.elapsedMs()
        Log.d(TAG, "[FLOW][DISEASE] Plant.id health fallback HTTP=${response.code()} elapsedMs=$elapsedMs success=${response.isSuccessful}")
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string().orEmpty()
            Log.e(TAG, "[FLOW][DISEASE] Plant.id health fallback error: code=${response.code()}, elapsedMs=$elapsedMs, errorBody=${errorBody.preview()}")
            throw PlantIdApiException(
                code = response.code(),
                errorBody = errorBody,
                message = response.code().toApiMessage(
                    defaultMessage = "No pudimos detectar una enfermedad con esta imagen, pero puedes continuar con el registro."
                )
            )
        }

        val body = response.body()
        Log.d(TAG, "[FLOW][DISEASE] Plant.id health fallback respuesta usada: body=${body.toString().preview()}")
        return body.toDiseaseDiagnosisResult()
    }

    private suspend fun testIdentificationApi(imageBase64: String): PlantApiTestResult {
        return runCatching {
            val response = identificationApi.identifyPlant(
                apiKey = ApiConfig.PLANT_ID_API_KEY,
                request = PlantIdRequestDto(images = listOf(imageBase64))
            )
            val body = response.body()
            val errorBody = response.errorBody()?.string().orEmpty()
            val commonName = body
                ?.result
                ?.classification
                ?.suggestions
                ?.maxByOrNull { it.probability ?: 0.0 }
                ?.toDomain()
                ?.commonName
            PlantApiTestResult(
                serviceName = "plant.id identification",
                success = response.isSuccessful,
                httpCode = response.code(),
                message = commonName ?: errorBody.ifBlank { "Respuesta recibida sin nombre común." },
                responsePreview = body?.toString()?.preview() ?: errorBody.preview()
            )
        }.getOrElse { error ->
            PlantApiTestResult(
                serviceName = "plant.id identification",
                success = false,
                httpCode = null,
                message = error.message ?: "Error desconocido"
            )
        }
    }

    private suspend fun testCropHealthApi(imageBase64: String): PlantApiTestResult {
        return runCatching {
            val response = diseaseApi.diagnoseDisease(
                apiKey = ApiConfig.PLANT_DISEASE_API_KEY,
                request = PlantDiseaseRequestDto(images = listOf(imageBase64))
            )
            val body = response.body()
            val errorBody = response.errorBody()?.string().orEmpty()
            val diagnosis = body.toDiseaseDiagnosisResult()
            PlantApiTestResult(
                serviceName = "crop.health",
                success = response.isSuccessful,
                httpCode = response.code(),
                message = diagnosis.diseaseName
                    ?: diagnosis.description
                    ?: errorBody.ifBlank { "Respuesta recibida sin enfermedad detectada." },
                responsePreview = body?.toString()?.preview() ?: errorBody.preview()
            )
        }.getOrElse { error ->
            PlantApiTestResult(
                serviceName = "crop.health",
                success = false,
                httpCode = null,
                message = error.message ?: "Error desconocido"
            )
        }
    }

    private suspend fun testPlantHealthApi(imageBase64: String): PlantApiTestResult {
        return runCatching {
            val response = identificationApi.identifyPlantWithHealth(
                apiKey = ApiConfig.PLANT_ID_API_KEY,
                health = healthQueryValue,
                request = PlantIdRequestDto(images = listOf(imageBase64))
            )
            val body = response.body()
            val errorBody = response.errorBody()?.string().orEmpty()
            val diagnosis = body.toDiseaseDiagnosisResult()
            PlantApiTestResult(
                serviceName = "plant.health",
                success = response.isSuccessful,
                httpCode = response.code(),
                message = diagnosis.diseaseName
                    ?: diagnosis.description
                    ?: errorBody.ifBlank { "Respuesta recibida sin diagnóstico de salud." },
                responsePreview = body?.toString()?.preview() ?: errorBody.preview()
            )
        }.getOrElse { error ->
            PlantApiTestResult(
                serviceName = "plant.health",
                success = false,
                httpCode = null,
                message = error.message ?: "Error desconocido"
            )
        }
    }

    private fun PlantSuggestionDto.toDomain(): PlantIdentificationResult {
        val scientific = name?.takeIf { it.isNotBlank() } ?: "Nombre científico no disponible"
        val common = details?.commonNames?.firstOrNull { it.isNotBlank() } ?: scientific
        return PlantIdentificationResult(
            commonName = common,
            scientificName = scientific,
            confidence = (probability ?: 0.0) * 100,
            imageUrl = details?.image?.value
                ?: similarImages?.firstOrNull { !it.url.isNullOrBlank() }?.url,
            description = details?.description?.value?.takeIf { it.isNotBlank() }
        )
    }

    private fun JsonObject?.toDiseaseDiagnosisResult(): DiseaseDiagnosisResult {
        if (this == null) {
            return DiseaseDiagnosisResult(null, null, null, null, null)
        }

        val result = safeObject("result") ?: this
        val disease = result.safeObject("disease")
            ?: result.safeObject("classification")
            ?: result
        val suggestions = disease.getAsJsonArray("suggestions")
        val topSuggestion = suggestions
            ?.mapNotNull { it.takeIf { item -> item.isJsonObject }?.asJsonObject }
            ?.maxByOrNull { it.getDoubleOrNull("probability") ?: 0.0 }

        val healthAssessment = result.safeObject("health_assessment")
            ?: result.safeObject("healthAssessment")
            ?: result.safeObject("health")
        val isHealthy = result.getBooleanOrNull("isHealthy")
            ?: result.getBooleanOrNull("is_healthy")
            ?: healthAssessment?.getBooleanOrNull("is_healthy")
            ?: healthAssessment?.getBooleanOrNull("isHealthy")
            ?: topSuggestion?.get("name")?.asString?.contains("healthy", ignoreCase = true)

        val details = topSuggestion?.safeObject("details")
        return DiseaseDiagnosisResult(
            isHealthy = isHealthy,
            diseaseName = topSuggestion?.getStringOrNull("name")
                ?: disease.getStringOrNull("name")
                ?: result.getStringOrNull("diseaseName"),
            probability = topSuggestion?.getDoubleOrNull("probability")
                ?: disease.getDoubleOrNull("probability")
                ?: result.getDoubleOrNull("probability"),
            description = details?.getStringOrNull("description")
                ?: disease.getStringOrNull("description")
                ?: result.getStringOrNull("description"),
            treatmentRecommendation = details?.getStringOrNull("treatment")
                ?: details?.getStringOrNull("treatmentRecommendation")
                ?: disease.getStringOrNull("treatmentRecommendation")
                ?: result.getStringOrNull("treatmentRecommendation")
        )
    }

    private fun JsonObject.safeObject(name: String): JsonObject? {
        return get(name)?.takeIf { it.isJsonObject }?.asJsonObject
    }

    private fun JsonObject.getStringOrNull(name: String): String? {
        return get(name)?.takeIf { !it.isJsonNull }?.asString?.takeIf { it.isNotBlank() }
    }

    private fun JsonObject.getDoubleOrNull(name: String): Double? {
        return runCatching {
            get(name)?.takeIf { !it.isJsonNull && it.isJsonPrimitive }?.asDouble
        }.getOrNull()
    }

    private fun JsonObject.getBooleanOrNull(name: String): Boolean? {
        return runCatching {
            get(name)?.takeIf { !it.isJsonNull && it.isJsonPrimitive }?.asBoolean
        }.getOrNull()
    }

    private fun String.normalizedBase64(): String {
        return substringAfter("base64,", this).trim()
    }

    private fun String.preview(maxLength: Int = 900): String {
        return if (length <= maxLength) this else take(maxLength) + "...(truncated)"
    }

    private fun logImagePayload(source: String, imageBase64: String) {
        Log.d(
            TAG,
            "$source image payload: base64NotEmpty=${imageBase64.isNotBlank()}, base64Length=${imageBase64.length}"
        )
    }

    private fun Int.toApiMessage(defaultMessage: String): String {
        return when (this) {
            400 -> "La imagen no parece válida para analizar."
            401, 403 -> "La API key no es válida o no tiene permisos."
            408 -> "La API tardó demasiado en responder."
            429 -> "Se alcanzó el límite de consultas de la API."
            in 500..599 -> "La API no responde en este momento."
            else -> defaultMessage
        }
    }

    private fun Long.elapsedMs(): Long = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this)

    private companion object {
        private const val TAG = "PlantRemoteDataSource"
    }
}
