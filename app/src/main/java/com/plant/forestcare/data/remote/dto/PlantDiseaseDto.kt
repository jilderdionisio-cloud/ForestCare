package com.plant.forestcare.data.remote.dto

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class PlantDiseaseRequestDto(
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("similar_images")
    val similarImages: Boolean = true
)

data class PlantDiseaseResponseDto(
    val isHealthy: Boolean? = null,
    val diseaseName: String? = null,
    val probability: Double? = null,
    val description: String? = null,
    val treatmentRecommendation: String? = null,
    val rawResponse: JsonObject? = null
)
