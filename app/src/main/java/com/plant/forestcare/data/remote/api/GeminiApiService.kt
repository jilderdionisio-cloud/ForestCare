package com.plant.forestcare.data.remote.api

import com.plant.forestcare.data.remote.dto.GeminiRequest
import com.plant.forestcare.data.remote.dto.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface GeminiApiService {
    @Headers("Content-Type: application/json")
    @POST("v1beta/models/gemini-flash-latest:generateContent")
    suspend fun generateContent(
        @Header("X-goog-api-key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}
