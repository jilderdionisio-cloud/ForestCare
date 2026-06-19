package com.plant.forestcare.data.remote.api

import com.google.gson.JsonObject
import com.plant.forestcare.data.remote.dto.PlantIdRequestDto
import com.plant.forestcare.data.remote.dto.PlantIdResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface PlantIdentificationApiService {
    @Headers("Content-Type: application/json")
    @POST("identification")
    suspend fun identifyPlant(
        @Header("Api-Key") apiKey: String,
        @Body request: PlantIdRequestDto
    ): Response<PlantIdResponseDto>

    @Headers("Content-Type: application/json")
    @POST("identification")
    suspend fun identifyPlantWithHealth(
        @Header("Api-Key") apiKey: String,
        @Query("health") health: String,
        @Body request: PlantIdRequestDto
    ): Response<JsonObject>
}
