package com.plant.forestcare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PlantApiResponseDto(
    @SerializedName("data")
    val data: List<PlantSpeciesDto> = emptyList(),
    @SerializedName("to")
    val to: Int? = null,
    @SerializedName("per_page")
    val perPage: Int? = null,
    @SerializedName("current_page")
    val currentPage: Int? = null,
    @SerializedName("from")
    val from: Int? = null,
    @SerializedName("last_page")
    val lastPage: Int? = null,
    @SerializedName("total")
    val total: Int? = null
)
