package com.example.x_project_android.viewmodels.tweet

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.x_project_android.data.models.Tweet

class SharedTweetViewModel : ViewModel() {
    var tweet by mutableStateOf<Tweet?>(null)
        private set

    fun setTweetShared(newTweet: Tweet) {
        tweet = newTweet
    }
}