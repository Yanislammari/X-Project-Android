package com.example.x_project_android.networking.services

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeDislikeService {
    @POST("opinion/like_comments/{comment_id}")
    fun likeComment(
        @Header("Authorization") authorization: String,
        @Path("comment_id") commentId: String): Call<Unit>

    @POST("opinion/dislike_comments/{comment_id}")
    fun dislikeComment(
        @Header("Authorization") authorization: String,
        @Path("comment_id") commentId: String): Call<Unit>

    @POST("opinion/like_tweets/{tweet_id}")
    fun likeTweet(
        @Header("Authorization") authorization: String,
        @Path("tweet_id") tweetId: String): Call<Unit>

    @POST("opinion/dislike_tweets/{tweet_id}")
    fun dislikeTweet(
        @Header("Authorization") authorization: String,
        @Path("tweet_id") tweetId: String): Call<Unit>
}