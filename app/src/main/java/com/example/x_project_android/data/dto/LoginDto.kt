package com.example.x_project_android.data.dto

data class LoginDto(
    val email: String,
    val password: String
)

data class LoginResponseDto(
    val token : String?
)