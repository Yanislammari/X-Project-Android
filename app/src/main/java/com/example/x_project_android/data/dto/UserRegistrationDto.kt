package com.example.x_project_android.data.dto

data class UserRegistrationDto(
    val bio: String,
    val pseudo: String,
    val imageUri: String?,
    val email: String,
    val password: String,
)