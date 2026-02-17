package com.example.biomarkerai.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Emulator uses 10.0.2.2 to access host localhost.
    // For physical device, use your computer's local IP (e.g., 192.168.1.15)
    // Make sure your backend is running on: python manage.py runserver 0.0.0.0:8000
    // private const val LOCAL_IP = "172.20.10.10" // Physical Device IP
    private const val LOCAL_IP = "10.0.2.2" // Google Emulator IP
    private const val BASE_URL = "http://$LOCAL_IP:8000/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
