package com.plant.forestcare.data

import android.content.Context
import com.plant.forestcare.data.local.AppDatabase
import com.plant.forestcare.data.local.PlantDao
import com.plant.forestcare.data.local.PlantEntity
import com.plant.forestcare.data.local.ReminderDao
import com.plant.forestcare.data.local.ReminderEntity
import com.plant.forestcare.data.remote.PlantRemoteDataSource
import com.plant.forestcare.data.local.storage.ImageStorage
import com.plant.forestcare.data.local.storage.ImageStorageManager
import com.plant.forestcare.domain.model.DiseaseDiagnosisResult
import com.plant.forestcare.domain.model.PlantApisConnectionTestResult
import com.plant.forestcare.domain.model.PlantIdentificationResult
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlantRepository private constructor(
    private val plantDao: PlantDao,
    private val reminderDao: ReminderDao,
    private val imageStorage: ImageStorage,
    private val remoteDataSource: PlantRemoteDataSource = PlantRemoteDataSource()
) {
    fun observePlants(): Flow<List<PlantEntity>> = plantDao.getAllPlantsFlow()

    fun getAllPlants(): Flow<List<PlantEntity>> = observePlants()

    fun getPlantById(id: String): Flow<PlantEntity?> = plantDao.getPlantById(id)

    fun getPendingReminders(): Flow<List<ReminderEntity>> = reminderDao.getPendingReminders()

    suspend fun savePlant(plant: PlantEntity) {
        withContext(Dispatchers.IO) {
            plantDao.insertPlant(plant)
            createCareReminders(plant)
        }
    }

    suspend fun createPlantWithImage(plant: PlantEntity, tempUri: android.net.Uri) {
        withContext(Dispatchers.IO) {
            val permanentPath = imageStorage.saveImage(tempUri)
            val plantToSave = plant.copy(
                photoUrl = permanentPath, // Usamos photoUrl tal como exige el expediente técnico
                updatedAt = System.currentTimeMillis()
            )
            plantDao.insertPlant(plantToSave)
            createCareReminders(plantToSave)
        }
    }

    suspend fun updatePlant(plant: PlantEntity) {
        withContext(Dispatchers.IO) {
            plantDao.updatePlant(plant)
            reminderDao.deleteRemindersForPlant(plant.id)
            createCareReminders(plant)
        }
    }

    suspend fun deletePlant(plant: PlantEntity) {
        withContext(Dispatchers.IO) {
            reminderDao.deleteRemindersForPlant(plant.id)
            plantDao.deletePlant(plant)
        }
    }

    suspend fun deletePlantComplete(plant: PlantEntity) {
        withContext(Dispatchers.IO) {
            plant.photoUrl?.let { imageStorage.deleteImage(it) }
            reminderDao.deleteRemindersForPlant(plant.id)
            plantDao.deletePlant(plant)
        }
    }

    suspend fun markWateringDone(plant: PlantEntity) {
        withContext(Dispatchers.IO) {
            val now = System.currentTimeMillis()
            val updated = plant.copy(
                nextWateringAt = now + plant.recommendedWateringDays.daysInMillis(),
                updatedAt = now
            )
            plantDao.updatePlant(updated)
            reminderDao.markRemindersCompleted(plant.id, REMINDER_WATERING)
            createCareReminders(updated)
        }
    }

    suspend fun completeReminder(reminderId: String) {
        withContext(Dispatchers.IO) { reminderDao.markReminderCompleted(reminderId) }
    }

    suspend fun postponeReminder(reminderId: String, days: Int = 1) {
        withContext(Dispatchers.IO) {
            reminderDao.rescheduleReminder(
                id = reminderId,
                scheduledAt = System.currentTimeMillis() + days.daysInMillis()
            )
        }
    }

    suspend fun skipReminder(reminderId: String) {
        withContext(Dispatchers.IO) { reminderDao.markReminderCompleted(reminderId) }
    }

    suspend fun identifyPlantFromImage(base64Image: String): Result<PlantIdentificationResult> {
        return withContext(Dispatchers.IO) {
            runCatching { remoteDataSource.identifyPlant(base64Image) }
        }
    }

    suspend fun diagnosePlantDisease(
        base64Image: String,
        identification: PlantIdentificationResult? = null
    ): Result<DiseaseDiagnosisResult> {
        return withContext(Dispatchers.IO) {
            runCatching { remoteDataSource.diagnoseDisease(base64Image, identification) }
        }
    }

    suspend fun testPlantApisConnection(base64Image: String): Result<PlantApisConnectionTestResult> {
        return withContext(Dispatchers.IO) {
            runCatching { remoteDataSource.testPlantApisConnection(base64Image) }
        }
    }

    private suspend fun createCareReminders(plant: PlantEntity) {
        val now = System.currentTimeMillis()
        val reminders = mutableListOf(
            ReminderEntity(
                    id = UUID.randomUUID().toString(),
                    plantId = plant.id,
                    type = REMINDER_WATERING,
                    title = "Regar ${plant.customName.ifBlank { plant.commonName }}",
                    scheduledAt = plant.nextWateringAt,
                    createdAt = now
            )
        )
        if (plant.healthStatus != "saludable") {
            reminders +=
                ReminderEntity(
                    id = UUID.randomUUID().toString(),
                    plantId = plant.id,
                    type = REMINDER_WEEKLY_REVIEW,
                    title = "Revisión semanal de ${plant.customName.ifBlank { plant.commonName }}",
                    scheduledAt = now + 7.daysInMillis(),
                    createdAt = now
                )
        }
        if (plant.healthStatus == "cuidado_urgente" && !plant.diseaseName.isNullOrBlank()) {
            reminders += ReminderEntity(
                id = UUID.randomUUID().toString(),
                plantId = plant.id,
                type = REMINDER_DISEASE_REVIEW,
                title = "Revisar enfermedad de ${plant.customName.ifBlank { plant.commonName }}",
                scheduledAt = plant.nextReviewAt,
                createdAt = now
            )
        }
        reminderDao.insertReminders(reminders)
    }

    private fun Int.daysInMillis(): Long = this * 24L * 60L * 60L * 1000L

    companion object {
        private const val REMINDER_WATERING = "watering"
        private const val REMINDER_WEEKLY_REVIEW = "weekly_review"
        private const val REMINDER_DISEASE_REVIEW = "revision_enfermedad"

        @Volatile
        private var INSTANCE: PlantRepository? = null

        fun getInstance(context: Context): PlantRepository {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getDatabase(context)
                val instance = PlantRepository(
                    plantDao = database.plantDao(),
                    reminderDao = database.reminderDao(),
                    imageStorage = ImageStorageManager(context)
                )
                INSTANCE = instance
                instance
            }
        }
    }
}
