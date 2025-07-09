package com.example.x_project_android.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String? = null,
    val pseudo: String? = null,
    val bio: String? = null,
    val imageUri: String? = null,
    val createdAt: Long? = null,
    val isSubscribed: Boolean = false,
):Parcelable