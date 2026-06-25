package com.plant.forestcare.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "care_history")
data class CareHistoryEntity(
    @PrimaryKey val id: String,
    val plantId: String,
    val userId: String?,
    val type: String,
    val title: String,
    val description: String,
    val performedAt: Long
)
