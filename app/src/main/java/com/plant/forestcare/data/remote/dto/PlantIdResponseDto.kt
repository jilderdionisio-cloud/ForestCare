package com.plant.forestcare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PlantIdResponseDto(
    @SerializedName("result")
    val result: PlantIdResultDto? = null
)

data class PlantIdResultDto(
    @SerializedName("classification")
    val classification: PlantClassificationDto? = null
)

data class PlantClassificationDto(
    @SerializedName("suggestions")
    val suggestions: List<PlantSuggestionDto> = emptyList()
)

data class PlantSuggestionDto(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("probability")
    val probability: Double? = null,
    @SerializedName("details")
    val details: PlantDetailsDto? = null,
    @SerializedName("similar_images")
    val similarImages: List<PlantSimilarImageDto>? = null
)

data class PlantDetailsDto(
    @SerializedName("common_names")
    val commonNames: List<String>? = null,
    @SerializedName("taxonomy")
    val taxonomy: PlantTaxonomyDto? = null,
    @SerializedName("description")
    val description: PlantDescriptionDto? = null,
    @SerializedName("image")
    val image: PlantImageDto? = null
)

data class PlantTaxonomyDto(
    @SerializedName("genus")
    val genus: String? = null,
    @SerializedName("family")
    val family: String? = null
)

data class PlantDescriptionDto(
    @SerializedName("value")
    val value: String? = null
)

data class PlantImageDto(
    @SerializedName("value")
    val value: String? = null
)

data class PlantSimilarImageDto(
    @SerializedName("url")
    val url: String? = null
)
