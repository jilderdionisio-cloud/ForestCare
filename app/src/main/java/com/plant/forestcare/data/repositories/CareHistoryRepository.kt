package com.plant.forestcare.data.repositories

import com.plant.forestcare.data.local.CareHistoryDao
import com.plant.forestcare.data.local.CareHistoryEntity
import java.util.UUID

class CareHistoryRepository(private val careHistoryDao: CareHistoryDao) {
    suspend fun logCare(plantId: String, type: String, title: String, description: String, userId: String?) {
        val entry = CareHistoryEntity(
            id = UUID.randomUUID().toString(),
            plantId = plantId,
            userId = userId,
            type = type,
            title = title,
            description = description,
            performedAt = System.currentTimeMillis()
        )
        careHistoryDao.insertHistory(entry)
    }

    fun getHistoryByPlant(plantId: String) = careHistoryDao.getHistoryByPlant(plantId)

    fun getAllHistory() = careHistoryDao.getAllHistory()
}
