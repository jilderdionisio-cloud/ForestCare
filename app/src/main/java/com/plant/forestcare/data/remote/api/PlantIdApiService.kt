package com.plant.forestcare.data.remote.api

import com.plant.forestcare.data.remote.dto.PlantIdRequestDto
import com.plant.forestcare.data.remote.dto.PlantIdResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PlantIdApiService {
    @POST("identification")
    suspend fun identifyPlant(
        @Header("Api-Key") apiKey: String,
        @Body request: PlantIdRequestDto
    ): Response<PlantIdResponseDto>
}
