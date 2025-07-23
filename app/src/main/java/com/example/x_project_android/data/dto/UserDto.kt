package com.example.x_project_android.data.dto

import com.example.x_project_android.data.models.User

data class UserDto(
    val id: String? = null,
    val pseudo: String? = null,
    val bio: String? = null,
    val profilePicture: String? = null,
    val createdAt: Long? = null,
    val isSubscribed: Boolean = false,
)

fun UserDto.toUser(): User {
    return User(
        id = this.id,
        pseudo = this.pseudo,
        bio = this.bio,
        imageUri = this.profilePicture,
        createdAt = this.createdAt,
        isSubscribed = this.isSubscribed
    )
}