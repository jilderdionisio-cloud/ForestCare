package com.plant.forestcare.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class PlantEntity(
    @PrimaryKey val id: String,
    val customName: String,
    val commonName: String,
    val scientificName: String,
    val description: String,
    val location: String,
    val sunlightExposure: String,
    val tags: String,
    val photoUri: String?,
    val healthStatus: String,
    val nextWateringText: String,
    val createdAt: Long,
    val updatedAt: Long
)
