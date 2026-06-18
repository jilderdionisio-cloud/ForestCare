package com.plant.forestcare.domain.model

data class PlantSpecies(
    val id: Int,
    val commonName: String,
    val scientificName: String,
    val imageUrl: String?,
    val sunlightExposure: String?,
    val watering: String?,
    val description: String
)
