package com.plant.forestcare.domain.model

data class PlantIdentificationResult(
    val commonName: String,
    val scientificName: String,
    val confidence: Double,
    val imageUrl: String?,
    val description: String?
)
