package com.example.x_project_android.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient


object RetrofitHttpClient {
    private const val BASE_URL = "http://192.168.1.126:3000/"

    private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS) // time to establish connection
    .readTimeout(15, TimeUnit.SECONDS)    // time to wait for server response
    .writeTimeout(15, TimeUnit.SECONDS)   // time to send data to server
    .build();

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Configured HTTp client's base url
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Added a converter for JSON --> DataClass
            .build()
    }
}