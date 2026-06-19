package com.plant.forestcare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiTextPart(
    @SerializedName("text")
    val text: String
)
