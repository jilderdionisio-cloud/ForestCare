package com.plant.forestcare.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val plantId: String = "",
    val type: String = "",
    val title: String = "",
    val scheduledAt: Long = 0L,
    val completed: Boolean = false,
    val createdAt: Long = 0L
)
