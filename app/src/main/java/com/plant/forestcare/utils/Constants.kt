package com.plant.forestcare.utils

data class FakePlant(
    val id: String,
    val name: String,
    val scientificName: String,
    val type: String,
    val location: String,
    val healthStatus: String,
    val healthPercent: Int,
    val wateringInfo: String,
    val lightInfo: String,
    val fertilizerInfo: String,
    val pruningInfo: String,
    val description: String
)

object Constants {
    val fakePlants = listOf(
        FakePlant(
            id = "1",
            name = "Monstera",
            scientificName = "Monstera deliciosa",
            type = "Interior",
            location = "Sala",
            healthStatus = "Saludable",
            healthPercent = 85,
            wateringInfo = "En 3 días",
            lightInfo = "Luz indirecta",
            fertilizerInfo = "15 Mar",
            pruningInfo = "Revisar",
            description = "Planta tropical de interior con hojas grandes. Necesita luz indirecta y riego moderado."
        ),
        FakePlant(
            id = "2",
            name = "Echeveria",
            scientificName = "Echeveria elegans",
            type = "Suculenta",
            location = "Balcón",
            healthStatus = "Excelente",
            healthPercent = 96,
            wateringInfo = "En 7 días",
            lightInfo = "Sol directo",
            fertilizerInfo = "30 Mar",
            pruningInfo = "No necesaria",
            description = "Suculenta resistente, ideal para espacios con mucha luz y poco riego."
        ),
        FakePlant(
            id = "3",
            name = "Ficus Lyrata",
            scientificName = "Ficus lyrata",
            type = "Interior",
            location = "Dormitorio",
            healthStatus = "Atención",
            healthPercent = 42,
            wateringInfo = "Hoy",
            lightInfo = "Luz media",
            fertilizerInfo = "Pendiente",
            pruningInfo = "Revisar hojas",
            description = "Planta decorativa de hojas grandes. Requiere humedad estable y buena iluminación."
        )
    )
}