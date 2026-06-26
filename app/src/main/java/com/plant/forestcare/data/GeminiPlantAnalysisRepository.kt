package com.plant.forestcare.data

import com.plant.forestcare.domain.model.GeminiPlantAnalysisInput
import com.plant.forestcare.data.GeminiConnectionCheckResult
import com.plant.forestcare.domain.model.PlantAnalysisResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiPlantAnalysisRepository @Inject constructor(
    private val service: GeminiPlantAnalysisService
) {
    suspend fun analyzePlant(
        plantName: String,
        scientificName: String?,
        diseaseName: String?,
        diseaseDescription: String?,
        leafStatus: String,
        soilHumidity: String,
        wateringFrequency: String,
        location: String
    ): PlantAnalysisResult {
        return service.analyzePlant(
            plantName = plantName,
            scientificName = scientificName,
            diseaseName = diseaseName,
            diseaseDescription = diseaseDescription,
            leafStatus = leafStatus,
            soilHumidity = soilHumidity,
            wateringFrequency = wateringFrequency,
            location = location
        )
    }

    suspend fun analyzePlant(input: GeminiPlantAnalysisInput): PlantAnalysisResult {
        return analyzePlant(
            plantName = input.commonName,
            scientificName = input.scientificName,
            diseaseName = input.diseaseDiagnosis?.diseaseName,
            diseaseDescription = input.diseaseDiagnosis?.description,
            leafStatus = input.leafStatus,
            soilHumidity = input.soilHumidity,
            wateringFrequency = input.currentWateringFrequency,
            location = input.locationType
        )
    }

    suspend fun verifyGeminiConnection(): GeminiConnectionCheckResult {
        return service.verifyGeminiConnection()
    }
}
