package com.plant.forestcare.di

import android.content.Context
import com.plant.forestcare.data.local.storage.ImageStorage
import com.plant.forestcare.data.local.storage.ImageStorageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideImageStorage(@ApplicationContext context: Context): ImageStorage {
        return ImageStorageManager(context)
    }
}
