package com.example.x_project_android.repositories

import android.util.Log
import com.example.x_project_android.data.dto.TweetDto
import com.example.x_project_android.data.dto.toTweetList
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.networking.RetrofitHttpClient
import com.example.x_project_android.networking.services.TweetService
import com.example.x_project_android.tokenStore.TokenManager
import com.example.x_project_android.viewmodels.tweet.TweetsResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TweetRepository {
    private val tweetService = RetrofitHttpClient.instance.create(TweetService::class.java)

    fun getTweets(callback: (TweetsResult) -> Unit) {
        if(TokenManager.getToken() == null) {
            callback(TweetsResult.Failure("An error occurred: No token found. Please log in again."))
            return
        }
        tweetService.getTweets(
            authToken = TokenManager.getToken()!!
        ).enqueue(object : Callback<List<TweetDto>> {
            override fun onResponse(call: Call<List<TweetDto>>, response: Response<List<TweetDto>>) {
                if (response.isSuccessful && response.code() == 200){
                    val tweetsDto = response.body()
                    if (tweetsDto != null) {
                        val tweets = tweetsDto.toTweetList()
                        callback(TweetsResult.Success("Success",tweets))
                    } else {
                        val listTweet = emptyList<Tweet>()
                        callback(TweetsResult.Success("No tweets found", listTweet))
                    }
                } else {
                    callback(TweetsResult.Failure("An error occurred while fetching tweets. Please try again later."))
                }
            }

            override fun onFailure(call: Call<List<TweetDto>>, t: Throwable) {
                callback(TweetsResult.Failure("An error occurred while fetching tweets. Please try again later."))
            }
        })
    }
}