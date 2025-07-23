package com.example.x_project_android.data.dto

import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User

data class TweetDto(
    val id: String? = null,
    val content: String? = null,
    val tweetPicture: String? = null,
    val user: UserDto? = null,
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
    val isDisliked: Boolean = false,
    val dislikeCount: Int = 0,
    val timestamp : Long? = System.currentTimeMillis(),
    val isCommented: Boolean = false,
)

fun TweetDto.toTweet(): Tweet {
    return Tweet(
        id = this.id,
        content = this.content,
        imageUri = this.tweetPicture,
        user = this.user?.toUser() ?: User(),
        isLiked = this.isLiked,
        likesCount = this.likeCount,
        isDisliked = this.isDisliked,
        dislikesCount = this.dislikeCount,
        timestamp = this.timestamp,
        isCommented = this.isCommented
    )
}

fun List<TweetDto>.toTweetList(): List<Tweet> {
    return this.map { it.toTweet() }
}