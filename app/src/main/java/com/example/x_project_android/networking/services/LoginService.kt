package com.example.x_project_android.networking.services

import com.example.x_project_android.data.dto.LoginDto
import retrofit2.Call
import retrofit2.http.POST

interface LoginService {
    @POST("login")
    fun getAllConversations(): Call<LoginDto>
}