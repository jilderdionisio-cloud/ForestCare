package com.plant.forestcare.data.remote.api

import com.google.gson.JsonObject
import com.plant.forestcare.data.remote.dto.PlantDiseaseRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PlantDiseaseApiService {
    @Headers("Content-Type: application/json")
    @POST("identification")
    suspend fun diagnoseDisease(
        @Header("Api-Key") apiKey: String,
        @Body request: PlantDiseaseRequestDto
    ): Response<JsonObject>
}
