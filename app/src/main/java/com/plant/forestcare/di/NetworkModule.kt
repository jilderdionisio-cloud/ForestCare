package com.plant.forestcare.di

import com.plant.forestcare.data.GeminiPlantAnalysisService
import com.plant.forestcare.data.remote.RetrofitInstance
import com.plant.forestcare.data.remote.api.PlantDiseaseApiService
import com.plant.forestcare.data.remote.api.PlantIdentificationApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providePlantIdentificationApiService(): PlantIdentificationApiService {
        return RetrofitInstance.plantIdentificationApiService
    }

    @Provides
    @Singleton
    fun providePlantDiseaseApiService(): PlantDiseaseApiService {
        return RetrofitInstance.plantDiseaseApiService
    }

    @Provides
    @Singleton
    fun provideGeminiPlantAnalysisService(): GeminiPlantAnalysisService {
        return RetrofitInstance.geminiPlantAnalysisService
    }
}
