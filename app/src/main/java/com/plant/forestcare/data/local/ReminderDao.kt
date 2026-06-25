package com.plant.forestcare.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE completed = 0 ORDER BY scheduledAt ASC")
    fun getPendingReminders(): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<ReminderEntity>)

    @Query("UPDATE reminders SET completed = 1 WHERE plantId = :plantId AND type = :type AND completed = 0")
    suspend fun markRemindersCompleted(plantId: String, type: String)

    @Query("UPDATE reminders SET completed = 1 WHERE id = :id")
    suspend fun markReminderCompleted(id: String)

    @Query("UPDATE reminders SET scheduledAt = :scheduledAt WHERE id = :id")
    suspend fun rescheduleReminder(id: String, scheduledAt: Long)

    @Query("DELETE FROM reminders WHERE plantId = :plantId")
    suspend fun deleteRemindersForPlant(plantId: String)
}
