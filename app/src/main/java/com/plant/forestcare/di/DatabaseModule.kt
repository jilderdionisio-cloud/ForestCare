package com.plant.forestcare.di

import android.content.Context
import com.plant.forestcare.data.local.AppDatabase
import com.plant.forestcare.data.local.CareHistoryDao
import com.plant.forestcare.data.local.PlantDao
import com.plant.forestcare.data.local.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun providePlantDao(database: AppDatabase): PlantDao {
        return database.plantDao()
    }

    @Provides
    fun provideReminderDao(database: AppDatabase): ReminderDao {
        return database.reminderDao()
    }

    @Provides
    fun provideCareHistoryDao(database: AppDatabase): CareHistoryDao {
        return database.careHistoryDao()
    }
}
