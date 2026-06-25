package com.plant.forestcare.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CareHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(entry: CareHistoryEntity)

    @Query("SELECT * FROM care_history WHERE plantId = :plantId ORDER BY performedAt DESC")
    fun getHistoryByPlant(plantId: String): Flow<List<CareHistoryEntity>>

    @Query("SELECT * FROM care_history ORDER BY performedAt DESC")
    fun getAllHistory(): Flow<List<CareHistoryEntity>>
}
