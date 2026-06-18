package com.plant.forestcare.data.remote.api

import com.plant.forestcare.data.remote.dto.PlantApiResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlantApiService {
    @GET("api/v2/species-list")
    suspend fun searchPlants(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): Response<PlantApiResponseDto>
}
