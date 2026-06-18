package com.plant.forestcare.data

import android.content.Context
import android.util.Log
import com.plant.forestcare.core.constants.AppConstants
import com.plant.forestcare.data.local.AppDatabase
import com.plant.forestcare.data.local.PlantDao
import com.plant.forestcare.data.local.PlantEntity
import com.plant.forestcare.data.remote.RetrofitInstance
import com.plant.forestcare.data.remote.api.PlantApiService
import com.plant.forestcare.data.remote.dto.PlantSpeciesDto
import com.plant.forestcare.domain.model.PlantSpecies
import kotlinx.coroutines.flow.Flow

class PlantRepository private constructor(
    private val plantDao: PlantDao,
    private val plantApiService: PlantApiService = RetrofitInstance.plantApiService
) {
    fun getAllPlants(): Flow<List<PlantEntity>> = plantDao.getAllPlants()

    fun getPlantById(id: String): Flow<PlantEntity?> = plantDao.getPlantById(id)

    suspend fun savePlant(plant: PlantEntity) {
        plantDao.insertPlant(plant)
    }

    suspend fun updatePlant(plant: PlantEntity) {
        plantDao.updatePlant(plant)
    }

    suspend fun deletePlant(plant: PlantEntity) {
        plantDao.deletePlant(plant)
    }

    suspend fun searchPlantsFromApi(query: String, page: Int = 1): List<PlantSpecies> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return emptyList()

        val response = plantApiService.searchPlants(
            apiKey = AppConstants.PERENUAL_API_KEY,
            query = normalizedQuery,
            page = page
        )

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string().orEmpty()
            Log.e(TAG, "Error HTTP Perenual: ${response.code()}")
            Log.e(TAG, "Cuerpo de error Perenual: $errorBody")
            throw PerenualApiException(
                code = response.code(),
                errorBody = errorBody,
                message = "Error HTTP ${response.code()} al consultar Perenual."
            )
        }

        Log.d("PerenualAPI", response.body().toString())
        return response.body()?.data.orEmpty().mapNotNull { it.toDomain() }
    }

    private fun PlantSpeciesDto.toDomain(): PlantSpecies? {
        val resolvedCommonName = commonName?.takeIf { it.isNotBlank() } ?: return null
        val resolvedScientificName = scientificName
            ?.firstOrNull { it.isNotBlank() }
            .orEmpty()
        val sunlightText = sunlight
            ?.filter { it.isNotBlank() }
            ?.joinToString(", ")

        return PlantSpecies(
            id = id ?: resolvedCommonName.hashCode(),
            commonName = resolvedCommonName,
            scientificName = resolvedScientificName,
            imageUrl = defaultImage?.regularUrl
                ?: defaultImage?.mediumUrl
                ?: defaultImage?.smallUrl
                ?: defaultImage?.originalUrl
                ?: defaultImage?.thumbnail,
            sunlightExposure = sunlightText?.toSpanishSunlightExposure(),
            watering = watering?.takeIf { it.isNotBlank() },
            description = buildDescription(resolvedCommonName, resolvedScientificName, watering, sunlightText)
        )
    }

    private fun String.toSpanishSunlightExposure(): String {
        val normalized = lowercase()
        return when {
            listOf("shade", "part shade", "filtered shade").any { normalized.contains(it) } -> "Baja"
            listOf("part sun", "medium", "indirect").any { normalized.contains(it) } -> "Media"
            listOf("full sun", "sun").any { normalized.contains(it) } -> "Alta"
            else -> this
        }
    }

    private fun buildDescription(
        commonName: String,
        scientificName: String,
        watering: String?,
        sunlight: String?
    ): String {
        val details = buildList {
            if (scientificName.isNotBlank()) add("Nombre científico: $scientificName")
            watering?.takeIf { it.isNotBlank() }?.let { add("Riego: $it") }
            sunlight?.takeIf { it.isNotBlank() }?.let { add("Luz: $it") }
        }
        return if (details.isEmpty()) {
            "Información encontrada en Perenual para $commonName."
        } else {
            details.joinToString(". ")
        }
    }

    companion object {
        private const val TAG = "PerenualApi"

        @Volatile
        private var INSTANCE: PlantRepository? = null

        fun getInstance(context: Context): PlantRepository {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getDatabase(context)
                val instance = PlantRepository(database.plantDao())
                INSTANCE = instance
                instance
            }
        }
    }
}

class PerenualApiException(
    val code: Int,
    val errorBody: String,
    override val message: String
) : Exception(message)
