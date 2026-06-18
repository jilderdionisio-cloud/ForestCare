package com.plant.forestcare.data.remote

import android.util.Log
import com.plant.forestcare.data.remote.api.PlantApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val TAG = "PerenualApi"
    private const val BASE_URL = "https://perenual.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val urlLoggingInterceptor = Interceptor { chain ->
        val request = chain.request()
        Log.d(TAG, "URL final Perenual: ${request.url}")
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(urlLoggingInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val plantApiService: PlantApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlantApiService::class.java)
    }
}
