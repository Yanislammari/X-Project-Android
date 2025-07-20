package com.example.x_project_android.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHttpClient {
    private const val BASE_URL = "http://192.168.1.126:3000/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Configured HTTp client's base url
            .addConverterFactory(GsonConverterFactory.create()) // Added a converter for JSON --> DataClass
            .build()
    }
}