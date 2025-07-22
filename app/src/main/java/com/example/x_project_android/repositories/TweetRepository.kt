package com.example.x_project_android.repositories

import android.util.Log
import com.example.x_project_android.data.dto.CommentDto
import com.example.x_project_android.data.dto.PostCommentDto
import com.example.x_project_android.data.dto.TweetDto
import com.example.x_project_android.data.dto.toComment
import com.example.x_project_android.data.dto.toCommentList
import com.example.x_project_android.data.dto.toTweet
import com.example.x_project_android.data.dto.toTweetList
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.networking.RetrofitHttpClient
import com.example.x_project_android.networking.services.TweetService
import com.example.x_project_android.tokenStore.TokenManager
import com.example.x_project_android.viewmodels.toRequestBody
import com.example.x_project_android.viewmodels.tweet.AddCommentResult
import com.example.x_project_android.viewmodels.tweet.AddTweetResult
import com.example.x_project_android.viewmodels.tweet.CommentResult
import com.example.x_project_android.viewmodels.tweet.TweetsResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    fun postTweets(content: String, tweetPicture: RequestBody, callback: (AddTweetResult) -> Unit) {
        if(TokenManager.getToken() == null) {
            callback(AddTweetResult.Failure("An error occurred: No token found. Please log in again."))
            return
        }
        val fileImage = MultipartBody.Part.createFormData(
            name = "tweetPicture", // field name expected by your backend
            filename = "image.jpg",  // any name works, real filename optional
            body = tweetPicture
        )
        val formDataContent = content.toRequestBody()
        val timestamp = System.currentTimeMillis().toString().toRequestBody()
        tweetService.postTweet(
            authToken = TokenManager.getToken()!!,
            content = formDataContent,
            timestamp = timestamp,
            tweetPicture = fileImage
        ).enqueue(object : Callback<TweetDto> {
            override fun onResponse(call: Call<TweetDto>, response: Response<TweetDto>) {
                if (response.isSuccessful && response.code() == 201){
                    val tweetDto = response.body()
                    if (tweetDto != null) {
                        val tweet = tweetDto.toTweet()
                        callback(AddTweetResult.Success("Success",tweet))
                    } else {
                        callback(AddTweetResult.Failure("An error occurred while uploading tweet. Please try again later."))
                    }
                } else {
                    callback(AddTweetResult.Failure("An error occurred while uploading tweet. Please try again later."))
                }
            }

            override fun onFailure(call: Call<TweetDto>, t: Throwable) {
                callback(AddTweetResult.Failure("An error occurred while uploading tweet. Please try again later."))
            }
        })
    }

    fun getComments(tweetId : String, callback: (CommentResult) -> Unit) {
        if(TokenManager.getToken() == null) {
            callback(CommentResult.Failure("An error occurred: No token found. Please log in again."))
            return
        }
        tweetService.getCommentsForTweet(
            authToken = TokenManager.getToken()!!,
            tweetId = tweetId
        ).enqueue(object : Callback<List<CommentDto>> {
            override fun onResponse(call: Call<List<CommentDto>>, response: Response<List<CommentDto>>) {
                if (response.isSuccessful && response.code() == 200){
                    val commentsDto = response.body()
                    if (commentsDto != null) {
                        val comments = commentsDto.toCommentList()
                        callback(CommentResult.Success("Success",comments))
                    } else {
                        callback(CommentResult.Failure("No comments found for this tweet."))
                    }
                } else {
                    callback(CommentResult.Failure("An error occurred while fetching comments. Please try again later."))
                }
            }

            override fun onFailure(call: Call<List<CommentDto>>, t: Throwable) {
                callback(CommentResult.Failure("An error occurred while fetching comments. Please try again later."))
            }
        })
    }


    fun postComment(tweetId: String,content: String, callback: (AddCommentResult) -> Unit) {
        if(TokenManager.getToken() == null) {
            callback(AddCommentResult.Failure("An error occurred: No token found. Please log in again."))
            return
        }
        tweetService.postComment(
            authToken = TokenManager.getToken()!!,
            tweetId = tweetId,
            postComment = PostCommentDto(content)
        ).enqueue(object : Callback<CommentDto> {
            override fun onResponse(call: Call<CommentDto>, response: Response<CommentDto>) {
                if (response.isSuccessful && response.code() == 201){
                    val commentDto = response.body()
                    if (commentDto != null) {
                        val comment = commentDto.toComment()
                        callback(AddCommentResult.Success("Tweet added successfully!", comment))
                    } else {
                        callback(AddCommentResult.Failure("An error occurred while posting comment. Please try again later."))
                    }
                } else {
                    callback(AddCommentResult.Failure("An error occurred while posting comment. Please try again later."))
                }
            }

            override fun onFailure(call: Call<CommentDto>, t: Throwable) {
                Log.d("TweetRepository", "Error posting comment: ${t.message}")
                callback(AddCommentResult.Failure("An error occurred while posting comment. Please try again later."))
            }
        })
    }
}