package com.example.x_project_android.event

import android.util.Log
import com.example.x_project_android.data.models.Comment
import com.example.x_project_android.data.models.User

object SendGlobalEvent {
    fun onLikeComment(commentId: String?) {
        if (commentId.isNullOrEmpty()) return
        GlobalEventBus.sendEvent(GlobalEvent.LikeComment(commentId))
    }
    fun onDislikeComment(commentId: String?) {
        if (commentId.isNullOrEmpty()) return
        GlobalEventBus.sendEvent(GlobalEvent.DislikeComment(commentId))
    }
    fun onAddComment(tweetId: String?, comment: Comment?) { // comment must come from API
        if (tweetId.isNullOrEmpty() || comment == null) return
        GlobalEventBus.sendEvent(GlobalEvent.AddComment(comment))
    }
    fun onLikeTweet(tweetId: String?) {
        if (tweetId.isNullOrEmpty()) return
        Log.d("SendGlobalEvent", "onLikeTweet: $tweetId")
        GlobalEventBus.sendEvent(GlobalEvent.Like(tweetId))
    }
    fun onDislikeTweet(tweetId: String?) {
        if (tweetId.isNullOrEmpty()) return
        GlobalEventBus.sendEvent(GlobalEvent.Dislike(tweetId))
    }
    fun onSubscribe(user: User?) {
        if (user == null) return
        GlobalEventBus.sendEvent(GlobalEvent.Subscribe(user))
    }
    fun onUnsubscribe(userId: String?) {
        if (userId.isNullOrEmpty()) return
        GlobalEventBus.sendEvent(GlobalEvent.Unsubscribe(userId))
    }
}