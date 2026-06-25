package com.plant.forestcare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiGenerateContentRequestDto(
    @SerializedName("contents")
    val contents: List<GeminiContentDto>,
    @SerializedName("generation_config")
    val generationConfig: GeminiGenerationConfigDto = GeminiGenerationConfigDto()
)

data class GeminiGenerationConfigDto(
    @SerializedName("temperature")
    val temperature: Double = 0.35,
    @SerializedName("response_mime_type")
    val responseMimeType: String = "application/json"
)

data class GeminiContentDto(
    @SerializedName("parts")
    val parts: List<GeminiPartDto>
)

data class GeminiPartDto(
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("inline_data")
    val inlineData: GeminiInlineDataDto? = null
)

data class GeminiInlineDataDto(
    @SerializedName("mime_type")
    val mimeType: String,
    @SerializedName("data")
    val data: String
)

data class GeminiGenerateContentResponseDto(
    @SerializedName("candidates")
    val candidates: List<GeminiCandidateDto> = emptyList()
)

data class GeminiCandidateDto(
    @SerializedName("content")
    val content: GeminiContentDto? = null
)
