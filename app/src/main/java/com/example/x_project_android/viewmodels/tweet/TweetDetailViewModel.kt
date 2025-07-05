package com.example.x_project_android.viewmodels.tweet

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import kotlinx.coroutines.*


object TweetDetailScreenDest {
    private const val ROUTE = "tweet_detail"
    const val TWEETIDARG = "tweetId"
    private const val TWEETIDPLACEHOLDER = "{$TWEETIDARG}"
    const val FULLROUTE = "$ROUTE/$TWEETIDPLACEHOLDER"
}

class TweetDetailViewModel: ViewModel() {
    private val _tweetId = mutableStateOf("")
    val tweetId: State<String> = _tweetId

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun setTweetId(id: String) {
        _tweetId.value = id
    }

    suspend fun fetchTweet(sharedViewModel: SharedTweetViewModel) {
        _isLoading.value = true
        val a = Tweet(
            id = _tweetId.value,
            content = "Voici mon chat Miaou !",
            imageUri = imageTest,
            user = User(
                id = "1",
                pseudo = "Alice",
                imageUri = imageTest,
            ),
            timestamp = System.currentTimeMillis() - 160 * 1000L,
            likesCount = 10,
            dislikesCount = 10,
        )
        delay(1000)
        setTweetInShared(a,sharedViewModel)
        _isLoading.value = false
    }

    private fun setTweetInShared(tweet: Tweet?, viewModel: SharedTweetViewModel) {
        viewModel.setTweet(tweet)
    }
}