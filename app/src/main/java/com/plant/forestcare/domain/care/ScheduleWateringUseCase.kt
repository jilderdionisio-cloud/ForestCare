package com.plant.forestcare.domain.care

import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.plant.forestcare.data.PlantRepository
import com.plant.forestcare.data.local.notification.WateringNotificationWorker
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleWateringUseCase @Inject constructor(
    private val plantRepository: PlantRepository,
    private val workManager: WorkManager
) {
    suspend fun execute(plantId: String) {
        val plant = plantRepository.getPlantById(plantId).first() ?: return
        
        // Frecuencia exacta de riego (ej. cada X días)
        // Convertimos los días a minutos para el delay inicial
        val delayInMinutes = plant.recommendedWateringDays * 24 * 60L
        
        val inputData = Data.Builder()
            .putString(WateringNotificationWorker.KEY_PLANT_NAME, plant.customName.ifBlank { plant.commonName })
            .build()
            
        val wateringWorkRequest = OneTimeWorkRequestBuilder<WateringNotificationWorker>()
            .setInitialDelay(15, TimeUnit.SECONDS)
            .setInputData(inputData)
            .addTag("watering_$plantId")
            .build()
            
        workManager.enqueue(wateringWorkRequest)
    }
}
