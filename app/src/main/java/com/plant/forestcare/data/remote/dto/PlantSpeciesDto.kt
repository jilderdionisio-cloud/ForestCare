package com.plant.forestcare.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PlantSpeciesDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("common_name")
    val commonName: String? = null,
    @SerializedName("scientific_name")
    val scientificName: List<String>? = null,
    @SerializedName("other_name")
    val otherName: List<String>? = null,
    @SerializedName("cycle")
    val cycle: String? = null,
    @SerializedName("watering")
    val watering: String? = null,
    @SerializedName("sunlight")
    val sunlight: List<String>? = null,
    @SerializedName("default_image")
    val defaultImage: PlantImageDto? = null
)

data class PlantImageDto(
    @SerializedName("image_id")
    val imageId: Int? = null,
    @SerializedName("license")
    val license: Int? = null,
    @SerializedName("license_name")
    val licenseName: String? = null,
    @SerializedName("license_url")
    val licenseUrl: String? = null,
    @SerializedName("original_url")
    val originalUrl: String? = null,
    @SerializedName("regular_url")
    val regularUrl: String? = null,
    @SerializedName("medium_url")
    val mediumUrl: String? = null,
    @SerializedName("small_url")
    val smallUrl: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null
)
