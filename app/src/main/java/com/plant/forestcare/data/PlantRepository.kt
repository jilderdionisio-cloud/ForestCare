package com.plant.forestcare.data

import android.content.Context
import com.plant.forestcare.data.local.AppDatabase
import com.plant.forestcare.data.local.PlantDao
import com.plant.forestcare.data.local.PlantEntity
import kotlinx.coroutines.flow.Flow

class PlantRepository private constructor(
    private val plantDao: PlantDao
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

    companion object {
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
