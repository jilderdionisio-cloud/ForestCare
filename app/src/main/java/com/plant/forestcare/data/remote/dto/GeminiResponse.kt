package com.plant.forestcare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiResponse(
    @SerializedName("candidates")
    val candidates: List<GeminiCandidate> = emptyList()
)

data class GeminiCandidate(
    @SerializedName("content")
    val content: GeminiContent? = null,
    @SerializedName("finishReason")
    val finishReason: String? = null
)
