package com.example.x_project_android.networking.services

import com.example.x_project_android.data.dto.LoginDto
import com.example.x_project_android.data.dto.LoginResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/login")
    fun login(@Body loginDto: LoginDto): Call<LoginResponseDto>
}