package com.plant.forestcare.data.diagnostics

import android.util.Log
import com.google.gson.JsonObject
import com.plant.forestcare.core.config.ApiConfig
import com.plant.forestcare.data.GeminiConnectionCheckResult
import com.plant.forestcare.data.GeminiPlantAnalysisService
import com.plant.forestcare.data.remote.RetrofitInstance
import com.plant.forestcare.data.remote.api.PlantDiseaseApiService
import com.plant.forestcare.data.remote.api.PlantIdentificationApiService
import com.plant.forestcare.data.remote.dto.PlantDiseaseRequestDto
import com.plant.forestcare.data.remote.dto.PlantIdRequestDto
import com.plant.forestcare.data.remote.dto.PlantIdResponseDto
import java.util.concurrent.TimeUnit
import retrofit2.Response

class ApiDiagnosticsRepository(
    private val plantIdentificationApi: PlantIdentificationApiService = RetrofitInstance.plantIdentificationApiService,
    private val diseaseApi: PlantDiseaseApiService = RetrofitInstance.plantDiseaseApiService,
    private val geminiApi: GeminiPlantAnalysisService = RetrofitInstance.geminiPlantAnalysisService
) {
    suspend fun verifyAllApis(imageBase64: String): ApiDiagnosticsReport {
        val normalizedImage = imageBase64.substringAfter("base64,", imageBase64).trim()
        val plant = verifyPlantIdentification(normalizedImage)
        val disease = verifyDiseaseApi(normalizedImage)
        val gemini = verifyGeminiApi(normalizedImage, plant, disease)
        return ApiDiagnosticsReport(
            plantIdentification = plant,
            diseaseDetection = disease,
            gemini = gemini
        )
    }

    private suspend fun verifyPlantIdentification(imageBase64: String): ApiDiagnosticResult {
        val url = "${ApiConfig.PLANT_ID_BASE_URL}identification"
        Log.d(PLANT_TAG, "Request URL=$url method=POST headers=Api-Key:<redacted>, Content-Type:application/json imageBase64Length=${imageBase64.length}")
        return timedCall(PLANT_TAG, url) {
            plantIdentificationApi.identifyPlant(
                apiKey = ApiConfig.PLANT_ID_API_KEY,
                request = PlantIdRequestDto(images = listOf(imageBase64))
            )
        }.toPlantDiagnostic(url)
    }

    private suspend fun verifyDiseaseApi(imageBase64: String): ApiDiagnosticResult {
        val url = "${ApiConfig.PLANT_DISEASE_BASE_URL}identification"
        Log.d(DISEASE_TAG, "Request URL=$url method=POST headers=Api-Key:<redacted>, Content-Type:application/json imageBase64Length=${imageBase64.length}")
        return timedCall(DISEASE_TAG, url) {
            diseaseApi.diagnoseDisease(
                apiKey = ApiConfig.PLANT_DISEASE_API_KEY,
                request = PlantDiseaseRequestDto(images = listOf(imageBase64))
            )
        }.toJsonDiagnostic(
            serviceName = "API Enfermedad",
            url = url,
            expectedFields = listOf("result", "disease", "suggestions")
        )
    }

    private suspend fun verifyGeminiApi(
        imageBase64: String,
        plantResult: ApiDiagnosticResult,
        diseaseResult: ApiDiagnosticResult
    ): ApiDiagnosticResult {
        val connection: GeminiConnectionCheckResult = geminiApi.verifyGeminiConnection()
        val url = "${ApiConfig.GEMINI_BASE_URL}v1beta/models/${ApiConfig.GEMINI_MODEL}:generateContent"
        val prompt = buildGeminiDiagnosticPrompt(plantResult, diseaseResult)
        Log.d(GEMINI_TAG, "Request URL=$url method=POST header=X-goog-api-key:<redacted> prompt=${prompt.preview()}")
        Log.d(GEMINI_TAG, "verifyGeminiConnection response=httpCode=${connection.httpCode} elapsedMs=${connection.elapsedMs} text=${connection.responseText.preview()}")
        return ApiDiagnosticResult(
            apiName = "Gemini",
            url = url,
            status = if (connection.success) ApiDiagnosticStatus.Working else ApiDiagnosticStatus.Error,
            httpCode = connection.httpCode,
            elapsedMs = connection.elapsedMs,
            responsePreview = connection.responseText.preview(),
            error = if (connection.success) null else connection.responseText,
            missingFields = if (connection.success) emptyList() else listOf("response")
        )
    }

    private suspend fun <T> timedCall(
        tag: String,
        url: String,
        call: suspend () -> Response<T>
    ): TimedResponse<T> {
        val startedAt = System.nanoTime()
        return try {
            val response = call()
            val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)
            val errorBody = if (response.isSuccessful) "" else response.errorBody()?.string().orEmpty()
            Log.d(tag, "Response URL=$url httpCode=${response.code()} elapsedMs=$elapsedMs errorBody=${errorBody.preview()} body=${response.body().toString().preview()}")
            TimedResponse(response, elapsedMs, errorBody, null)
        } catch (error: Throwable) {
            val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)
            Log.e(tag, "Error URL=$url elapsedMs=$elapsedMs message=${error.message}", error)
            TimedResponse(null, elapsedMs, "", error)
        }
    }

    private fun TimedResponse<PlantIdResponseDto>.toPlantDiagnostic(url: String): ApiDiagnosticResult {
        val response = response
        val suggestion = response
            ?.body()
            ?.result
            ?.classification
            ?.suggestions
            ?.maxByOrNull { it.probability ?: 0.0 }
        val commonName = suggestion?.details?.commonNames?.firstOrNull()
        val scientificName = suggestion?.name
        val complete = response?.isSuccessful == true &&
            !commonName.isNullOrBlank() &&
            !scientificName.isNullOrBlank() &&
            suggestion?.probability != null
        return ApiDiagnosticResult(
            apiName = "API Identificación",
            url = url,
            status = statusFor(response?.isSuccessful == true, complete, error),
            httpCode = response?.code(),
            elapsedMs = elapsedMs,
            responsePreview = listOfNotNull(
                "commonName=$commonName",
                "scientificName=$scientificName",
                "confidence=${suggestion?.probability}"
            ).joinToString(", "),
            error = error?.message ?: errorBody.takeIf { it.isNotBlank() },
            missingFields = listOfNotNull(
                "commonName".takeIf { commonName.isNullOrBlank() },
                "scientificName".takeIf { scientificName.isNullOrBlank() },
                "confidence".takeIf { suggestion?.probability == null }
            )
        )
    }

    private fun TimedResponse<JsonObject>.toJsonDiagnostic(
        serviceName: String,
        url: String,
        expectedFields: List<String>
    ): ApiDiagnosticResult {
        val response = response
        val bodyText = response?.body()?.toString().orEmpty()
        val missingFields = expectedFields.filterNot { bodyText.contains(it, ignoreCase = true) }
        val complete = response?.isSuccessful == true && missingFields.isEmpty()
        return ApiDiagnosticResult(
            apiName = serviceName,
            url = url,
            status = statusFor(response?.isSuccessful == true, complete, error),
            httpCode = response?.code(),
            elapsedMs = elapsedMs,
            responsePreview = bodyText.preview(),
            error = error?.message ?: errorBody.takeIf { it.isNotBlank() },
            missingFields = missingFields
        )
    }

    private fun buildGeminiDiagnosticPrompt(
        plantResult: ApiDiagnosticResult,
        diseaseResult: ApiDiagnosticResult
    ): String {
        return """
            Devuelve SOLO JSON válido con:
            diagnosis, healthStatus, wateringRecommendation, lightRecommendation,
            fertilizationRecommendation, pruningRecommendation, treatmentRecommendation.

            Datos de identificación:
            ${plantResult.responsePreview}

            Diagnóstico de enfermedad:
            ${diseaseResult.responsePreview.ifBlank { diseaseResult.error.orEmpty() }}

            Respuestas del usuario:
            ubicación=Interior, humedad=Muy mojada, hojas=Amarillas, frecuencia de riego=Semanal.
        """.trimIndent()
    }

    private fun statusFor(success: Boolean, complete: Boolean, error: Throwable?): ApiDiagnosticStatus {
        return when {
            error != null || !success -> ApiDiagnosticStatus.Error
            complete -> ApiDiagnosticStatus.Working
            else -> ApiDiagnosticStatus.IncompleteResponse
        }
    }

    private fun String.preview(maxLength: Int = 900): String {
        return if (length <= maxLength) this else take(maxLength) + "...(truncated)"
    }

    private data class TimedResponse<T>(
        val response: Response<T>?,
        val elapsedMs: Long,
        val errorBody: String,
        val error: Throwable?
    )

    private companion object {
        private const val PLANT_TAG = "[API_CHECK][PLANT_ID]"
        private const val DISEASE_TAG = "[API_CHECK][DISEASE]"
        private const val GEMINI_TAG = "[API_CHECK][GEMINI]"
    }
}

data class ApiDiagnosticsReport(
    val plantIdentification: ApiDiagnosticResult,
    val diseaseDetection: ApiDiagnosticResult,
    val gemini: ApiDiagnosticResult
)

data class ApiDiagnosticResult(
    val apiName: String,
    val url: String,
    val status: ApiDiagnosticStatus,
    val httpCode: Int?,
    val elapsedMs: Long,
    val responsePreview: String,
    val error: String?,
    val missingFields: List<String> = emptyList()
)

enum class ApiDiagnosticStatus(val label: String) {
    Working("Funcionando"),
    IncompleteResponse("Respuesta incompleta"),
    Error("Error")
}
