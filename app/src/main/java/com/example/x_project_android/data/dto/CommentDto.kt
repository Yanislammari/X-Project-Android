package com.example.x_project_android.data.dto

import com.example.x_project_android.data.models.Comment
import com.example.x_project_android.data.models.Tweet

data class PostCommentDto(
    val content: String,
    val timestamp: Long? = System.currentTimeMillis()
)

data class CommentDto(
    val id: String? = null,
    val tweetId: String? = null,
    val content: String? = null,
    val user: UserDto? = null,
    val timestamp: Long? = System.currentTimeMillis(),
    val likeCount: Int = 0,
    val dislikeCount: Int = 0,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false
)

fun CommentDto.toComment() = Comment(
    id = id,
    tweetId = tweetId,
    content = content,
    user = user?.toUser() ?: UserDto().toUser(),
    timestamp = timestamp ?: System.currentTimeMillis(),
    likesCount = likeCount,
    dislikesCount = dislikeCount,
    isLiked = isLiked,
    isDisliked = isDisliked
)

fun List<CommentDto>.toCommentList(): List<Comment> {
    return this.map { it.toComment() }
}

