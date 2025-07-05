package com.example.x_project_android.data.models

data class Comment(
    val id: String? = null,
    val tweetId: String? = null,
    val content: String? = null,
    val user: User,
    val timestamp: Long? = System.currentTimeMillis(),
    val likesCount: Int = 0,
    val dislikesCount: Int = 0,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false
)