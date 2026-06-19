package com.plant.forestcare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiRequest(
    @SerializedName("contents")
    val contents: List<GeminiContent>
)
