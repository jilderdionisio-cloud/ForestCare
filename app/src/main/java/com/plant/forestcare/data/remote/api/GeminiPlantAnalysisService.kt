package com.plant.forestcare.data.remote.api

import com.plant.forestcare.data.remote.dto.GeminiGenerateContentRequestDto
import com.plant.forestcare.data.remote.dto.GeminiGenerateContentResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GeminiPlantAnalysisService {
    @Headers("Content-Type: application/json")
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generatePlantAnalysis(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiGenerateContentRequestDto
    ): Response<GeminiGenerateContentResponseDto>
}
