package com.plant.forestcare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PlantIdRequestDto(
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("similar_images")
    val similarImages: Boolean = true
)
