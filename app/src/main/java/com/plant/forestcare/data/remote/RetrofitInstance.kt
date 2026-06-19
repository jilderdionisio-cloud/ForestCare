package com.plant.forestcare.data.remote

import android.util.Log
import com.plant.forestcare.data.GeminiPlantAnalysisService
import com.plant.forestcare.core.config.ApiConfig
import com.plant.forestcare.data.remote.api.GeminiApiService
import com.plant.forestcare.data.remote.api.PlantDiseaseApiService
import com.plant.forestcare.data.remote.api.PlantIdApiService
import com.plant.forestcare.data.remote.api.PlantIdentificationApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val TAG = "PlantApi"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        redactHeader("Api-Key")
        redactHeader("X-goog-api-key")
        redactHeader("Authorization")
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val networkDebugInterceptor = Interceptor { chain ->
        val request = chain.request()
        val startedAt = System.nanoTime()
        val redactedUrl = if ("key" in request.url.queryParameterNames) {
            request.url.newBuilder()
                .setQueryParameter("key", "REDACTED")
                .build()
        } else {
            request.url
        }
        Log.d(TAG, "HTTP -> ${request.method} $redactedUrl")
        val response = chain.proceed(request)
        val elapsedMs = (System.nanoTime() - startedAt) / 1_000_000
        Log.d(TAG, "HTTP <- ${response.code} ${request.method} $redactedUrl (${elapsedMs}ms)")
        response
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .addInterceptor(networkDebugInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val plantIdApiService: PlantIdApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.PLANT_ID_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlantIdApiService::class.java)
    }

    val plantIdentificationApiService: PlantIdentificationApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.PLANT_ID_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlantIdentificationApiService::class.java)
    }

    val plantDiseaseApiService: PlantDiseaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.PLANT_DISEASE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlantDiseaseApiService::class.java)
    }

    val geminiApiService: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.GEMINI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }

    val geminiPlantAnalysisService: GeminiPlantAnalysisService by lazy {
        GeminiPlantAnalysisService(geminiApiService)
    }
}
