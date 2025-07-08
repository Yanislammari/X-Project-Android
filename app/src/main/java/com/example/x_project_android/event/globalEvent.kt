package com.example.x_project_android.event

import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import kotlinx.coroutines.flow.MutableSharedFlow

object NavEventBus {
    private val _events = MutableSharedFlow<NavEvent>(replay = 1)
    val events = _events

    fun sendEvent(event: NavEvent) {
        _events.tryEmit(event)
    }
}

object GlobalEventBus {
    private val _events = MutableSharedFlow<GlobalEvent>(replay = 1)
    val events = _events

    fun sendEvent(event: GlobalEvent) {
        _events.tryEmit(event)
    }
}


sealed class GlobalEvent {
    data class Unsubscribe(val userId: String) : GlobalEvent()
    data class Subscribe(val userId: String) : GlobalEvent()
    data class Like(val tweetId: String) : GlobalEvent()
    data class Dislike(val tweetId: String) : GlobalEvent()
    data class AddComment(val tweetId: String) : GlobalEvent()
    data class LikeComment(val commentId: String) : GlobalEvent()
    data class DislikeComment(val commentId: String) : GlobalEvent()
    data class AddTweet(val tweet: Tweet) : GlobalEvent()
}

sealed class NavEvent {
    data class TweetDetail(val tweet: Tweet): NavEvent()
    data class SubscribeDetail(val user: User?): NavEvent()
}