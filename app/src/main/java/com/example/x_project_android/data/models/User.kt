package com.example.x_project_android.data.models

data class User(
    val id: String? = null,
    val pseudo: String? = null,
    val bio: String? = null,
    val imageUri: String? = null,
    val createdAt: Long? = null,
    val isSubscribed: Boolean = false,
)