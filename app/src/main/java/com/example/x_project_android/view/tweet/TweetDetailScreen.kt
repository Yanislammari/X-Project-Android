package com.example.x_project_android.view.tweet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.x_project_android.view.compose.DisplayLoader
import com.example.x_project_android.viewmodels.tweet.SharedTweetViewModel
import com.example.x_project_android.viewmodels.tweet.TweetDetailViewModel
import com.example.x_project_android.viewmodels.tweet.TweetsViewModel


@Composable
fun TweetDetailScreen(
    navHostController: NavHostController,
    tweetsViewModel: TweetsViewModel,
    tweetDetailViewModel: TweetDetailViewModel,
    sharedTweetViewModel: SharedTweetViewModel
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(tweetDetailViewModel.tweetId.value) {
        if (sharedTweetViewModel.tweet.value == null) {
            tweetDetailViewModel.fetchTweet(sharedTweetViewModel)
        }
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (tweetDetailViewModel.isLoading.value) {
                DisplayLoader()
            } else {
                sharedTweetViewModel.tweet.value?.let {
                    TweetCell(
                        tweet = it,
                        onLike = {
                            val tweet = tweetsViewModel.likeTweet(tweetDetailViewModel.tweetId.value)
                            if (tweet != null) {
                                sharedTweetViewModel.setTweet(tweet)
                            }
                        },
                        onDislike = {
                            val tweet =
                                tweetsViewModel.dislikeTweet(tweetDetailViewModel.tweetId.value)
                            if (tweet != null) {
                                sharedTweetViewModel.setTweet(tweet)
                            }
                        },
                        searchValue = "",
                        onClick = {},
                        maxLength = 1000,
                    )
                } ?: Text("Tweet not found.")
            }
        }
    }
}