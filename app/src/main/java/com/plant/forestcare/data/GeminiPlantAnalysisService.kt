package com.plant.forestcare.data

import android.util.Log
import com.plant.forestcare.data.remote.api.GeminiApiService
import com.plant.forestcare.data.remote.dto.GeminiContent
import com.plant.forestcare.data.remote.dto.GeminiRequest
import com.plant.forestcare.data.remote.dto.GeminiTextPart
import com.plant.forestcare.domain.model.GeminiPlantAnalysisException
import com.plant.forestcare.domain.model.GeminiPlantAnalysisInput
import com.plant.forestcare.domain.model.PlantAnalysisResult
import java.util.concurrent.TimeUnit

class GeminiPlantAnalysisService(
    private val apiService: GeminiApiService
) {
    suspend fun analyzePlant(input: GeminiPlantAnalysisInput): PlantAnalysisResult {
        return analyzePlant(
            plantName = input.commonName,
            scientificName = input.scientificName,
            diseaseName = input.diseaseDiagnosis?.diseaseName,
            diseaseDescription = input.diseaseDiagnosis?.description,
            leafStatus = input.leafStatus,
            soilHumidity = input.soilHumidity,
            wateringFrequency = input.currentWateringFrequency,
            location = input.locationType
        )
    }

    suspend fun analyzePlant(
        plantName: String,
        scientificName: String?,
        diseaseName: String?,
        diseaseDescription: String?,
        leafStatus: String,
        soilHumidity: String,
        wateringFrequency: String,
        location: String
    ): PlantAnalysisResult {
        val prompt = buildPrompt(
            plantName = plantName,
            scientificName = scientificName,
            diseaseName = diseaseName,
            diseaseDescription = diseaseDescription,
            leafStatus = leafStatus,
            soilHumidity = soilHumidity,
            wateringFrequency = wateringFrequency,
            location = location
        )
        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiTextPart(text = prompt)
                    )
                )
            )
        )

        val startedAt = System.nanoTime()
        Log.d(TAG, "[GEMINI_API] Request URL=${ENDPOINT} image=optional=false prompt=${prompt.preview()}")
        val response = apiService.generateContent(
            apiKey = com.plant.forestcare.core.config.ApiConfig.GEMINI_API_KEY,
            request = request
        )
        val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string().orEmpty()
            Log.e(TAG, "[GEMINI_API] URL=$ENDPOINT HTTP=${response.code()} elapsedMs=$elapsedMs errorBody=${errorBody.preview()}")
            throw GeminiPlantAnalysisException(
                code = response.code(),
                errorBody = errorBody,
                message = response.code().toGeminiMessage()
            )
        }

        val text = response.body()
            ?.candidates
            ?.firstOrNull()
            ?.content
            ?.parts
            ?.firstNotNullOfOrNull { it.text }
            .orEmpty()

        Log.d(TAG, "[GEMINI_API] URL=$ENDPOINT HTTP=${response.code()} elapsedMs=$elapsedMs response=${text.preview()}")
        return parseAnalysis(text)
    }

    suspend fun verifyGeminiConnection(): GeminiConnectionCheckResult {
        val prompt = "Responde únicamente la palabra OK"
        val startedAt = System.nanoTime()
        Log.d(TAG, "[GEMINI_API] verifyGeminiConnection request URL=$ENDPOINT prompt=$prompt")
        return try {
            val response = apiService.generateContent(
                apiKey = com.plant.forestcare.core.config.ApiConfig.GEMINI_API_KEY,
                request = GeminiRequest(
                    contents = listOf(
                        GeminiContent(parts = listOf(GeminiTextPart(text = prompt)))
                    )
                )
            )
            val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)
            val bodyText = response.body()
                ?.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstNotNullOfOrNull { it.text }
                .orEmpty()
            val errorBody = response.errorBody()?.string().orEmpty()
            Log.d(TAG, "[GEMINI_API] verifyGeminiConnection URL=$ENDPOINT HTTP=${response.code()} elapsedMs=$elapsedMs response=${bodyText.preview()} errorBody=${errorBody.preview()}")
            GeminiConnectionCheckResult(
                httpCode = response.code(),
                elapsedMs = elapsedMs,
                responseText = bodyText.ifBlank { errorBody },
                success = response.isSuccessful
            )
        } catch (error: Throwable) {
            val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)
            Log.e(TAG, "[GEMINI_API] verifyGeminiConnection URL=$ENDPOINT elapsedMs=$elapsedMs error=${error.message}", error)
            GeminiConnectionCheckResult(
                httpCode = null,
                elapsedMs = elapsedMs,
                responseText = error.message.orEmpty(),
                success = false
            )
        }
    }

    private fun parseAnalysis(text: String): PlantAnalysisResult {
        val cleaned = text
            .trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        return runCatching {
            gson.fromJson(cleaned, PlantAnalysisResult::class.java)
        }.getOrElse {
            PlantAnalysisResult(
                healthStatus = "requiere_atencion",
                diagnosis = cleaned.ifBlank { "Gemini no devolvió un diagnóstico estructurado." },
                possibleCauses = emptyList(),
                treatmentRecommendation = cleaned,
                monthlyPlan = emptyList()
            )
        }
    }

    private fun buildPrompt(
        plantName: String,
        scientificName: String?,
        diseaseName: String?,
        diseaseDescription: String?,
        leafStatus: String,
        soilHumidity: String,
        wateringFrequency: String,
        location: String
    ): String {
        return """
            Eres un experto botánico.

            Analiza esta información:

            Nombre común:
            $plantName

            Nombre científico:
            ${scientificName ?: "No disponible"}

            Estado hojas:
            $leafStatus

            Humedad:
            $soilHumidity

            Frecuencia de riego:
            $wateringFrequency

            Ubicación:
            $location

            Diagnóstico API:
            ${diseaseName ?: "Sin enfermedad detectada"}

            Descripción API:
            ${diseaseDescription ?: "No disponible"}

            Genera:
            1. Estado de salud.
            2. Diagnóstico.
            3. Posibles causas.
            4. Recomendación de riego.
            5. Recomendación de luz.
            6. Recomendación de fertilización.
            7. Recomendación de poda.
            8. Plan de cuidados de 30 días.

            Responde en formato JSON con estas claves exactas:
            {
              "healthStatus": "",
              "diagnosis": "",
              "possibleCauses": [],
              "wateringRecommendation": "",
              "lightRecommendation": "",
              "fertilizationRecommendation": "",
              "pruningRecommendation": "",
              "treatmentRecommendation": "",
              "monthlyPlan": []
            }
        """.trimIndent()
    }

    private fun Int.toGeminiMessage(): String {
        return when (this) {
            400 -> "Gemini no pudo procesar la solicitud."
            401, 403 -> "La API key de Gemini no es válida o no tiene permisos."
            404 -> "El endpoint o modelo de Gemini no existe o no está disponible."
            408 -> "Gemini tardó demasiado en responder."
            429 -> "Gemini no tiene cuota disponible."
            in 500..599 -> "Gemini no responde en este momento."
            else -> "No se pudo completar el análisis inteligente."
        }
    }

    private fun String.preview(maxLength: Int = 900): String {
        return if (length <= maxLength) this else take(maxLength) + "...(truncated)"
    }

    companion object {
        private const val TAG = "GeminiPlantAnalysis"
        private const val ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent"
        private val gson = com.google.gson.Gson()
    }
}

data class GeminiConnectionCheckResult(
    val httpCode: Int?,
    val elapsedMs: Long,
    val responseText: String,
    val success: Boolean
)
