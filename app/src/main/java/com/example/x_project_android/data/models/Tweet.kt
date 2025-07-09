package com.example.x_project_android.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tweet(
    val id: String? = null,
    val content: String? = null,
    val imageUri: String? = null,
    val user: User,
    val isLiked: Boolean = false,
    val likesCount: Int = 0,
    val isDisliked: Boolean = false,
    val dislikesCount: Int = 0,
    val timestamp : Long? = System.currentTimeMillis(),
    val isCommented: Boolean = false,
):Parcelable
