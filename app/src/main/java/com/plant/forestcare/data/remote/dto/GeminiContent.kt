package com.plant.forestcare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiContent(
    @SerializedName("parts")
    val parts: List<GeminiTextPart>
)
