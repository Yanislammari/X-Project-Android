package com.example.x_project_android.event

import com.example.x_project_android.data.models.Tweet
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
    fun onLikeTweet(tweetId: String?) {
        if (tweetId.isNullOrEmpty()) return
        GlobalEventBus.sendEvent(GlobalEvent.Like(tweetId))
    }
    fun onDislikeTweet(tweetId: String?) {
        if (tweetId.isNullOrEmpty()) return
        GlobalEventBus.sendEvent(GlobalEvent.Dislike(tweetId))
    }
}

object SendNavEvent {
    fun onTweetDetail(tweet: Tweet?) {
        if (tweet == null) return
        NavEventBus.sendEvent(NavEvent.TweetDetail(tweet))
    }
    fun onSubscribeDetail(user: User?) {
        NavEventBus.sendEvent(NavEvent.SubscribeDetail(user))
    }
}