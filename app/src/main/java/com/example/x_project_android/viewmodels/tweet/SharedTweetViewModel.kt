package com.example.x_project_android.viewmodels.tweet

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.x_project_android.data.models.Tweet

class SharedTweetViewModel:ViewModel() {
    private val  _tweet = mutableStateOf<Tweet?>(null)
    val tweet: State<Tweet?> = _tweet

    fun setTweet(tweet: Tweet?) {
        _tweet.value = tweet
    }

    fun markTweetAsCommented() {
        _tweet.value?.let { currentTweet ->
            val updatedTweet = currentTweet.copy(isCommented = true)
            _tweet.value = updatedTweet
        }
    }
}