package com.example.x_project_android.networking.services

import com.example.x_project_android.data.dto.CommentDto
import com.example.x_project_android.data.dto.PostCommentDto
import retrofit2.Call
import com.example.x_project_android.data.dto.TweetDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface TweetService {
    @GET("tweets/tweets")
    fun getTweets(@Header("Authorization") authToken: String): Call<List<TweetDto>>

    @GET("comment/comments/{tweetId}")
    fun getCommentsForTweet(
        @Header("Authorization") authToken: String,
        @Path("tweetId") tweetId: String
    ): Call<List<CommentDto>>

    @Multipart
    @POST("tweets/tweets")
    fun postTweet(
        @Header("Authorization") authToken: String,
        @Part("content") content: RequestBody,
        @Part("timestamp") timestamp: RequestBody,
        @Part tweetPicture: MultipartBody.Part?,
    ): Call<TweetDto>

    @POST("comment/comments/{tweetId}")
    fun postComment(
        @Header("Authorization") authToken: String,
        @Path("tweetId") tweetId: String,
        @Body postComment: PostCommentDto,
    ): Call<CommentDto>
}