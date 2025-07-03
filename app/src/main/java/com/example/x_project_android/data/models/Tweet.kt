package com.example.x_project_android.data.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Tweet(
    val id: String? = null,
    val content: String? = null,
    val imageUri: String? = null,
    val user: User,
    isLiked: Boolean = false,
    likesCount: Int = 0,
    isDisliked: Boolean = false,
    dislikesCount: Int = 0,
    val timestamp : Long? = System.currentTimeMillis(),
) {
    var isLiked by mutableStateOf(isLiked)
    var likesCount by mutableIntStateOf(likesCount)
    var isDisliked by mutableStateOf(isDisliked)
    var dislikesCount by mutableIntStateOf(dislikesCount)
}