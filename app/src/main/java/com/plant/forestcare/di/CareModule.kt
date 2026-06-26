package com.plant.forestcare.di

import android.content.Context
import androidx.work.WorkManager
import com.plant.forestcare.data.PlantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CareModule {

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun providePlantRepository(@ApplicationContext context: Context): PlantRepository {
        return PlantRepository.getInstance(context)
    }
}
