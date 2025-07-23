package com.example.x_project_android.repositories

import android.util.Log
import com.example.x_project_android.networking.RetrofitHttpClient
import com.example.x_project_android.networking.services.LikeDislikeService
import com.example.x_project_android.tokenStore.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeDislikeRepository() {
    private val likeAndDisService = RetrofitHttpClient.instance.create(LikeDislikeService::class.java)


    fun likeComment(commentId: String) {
        if(TokenManager.getToken() == null) {
            return
        }
        likeAndDisService.likeComment(TokenManager.getToken()!!,commentId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                // onSuccess (intentionally left empty)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // onFailure (intentionally left empty)
            }
        })
    }

    fun dislikeComment(commentId: String) {
        if(TokenManager.getToken() == null) {
            return
        }
        likeAndDisService.dislikeComment(TokenManager.getToken()!!,commentId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // onFailure (intentionally left empty)
            }
        })
    }

    fun likeTweet(tweetId: String) {
        if(TokenManager.getToken() == null) {
            return
        }
        likeAndDisService.likeTweet(TokenManager.getToken()!!,tweetId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.d("LikeDislikeRepository", "like comment successful: ${response}")
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("LikeDislikeRepository", "Dislike comment successful: ${t.message}")
            }
        })
    }

    fun dislikeTweet(tweetId: String) {
        if(TokenManager.getToken() == null) {
            return
        }
        likeAndDisService.dislikeTweet(TokenManager.getToken()!!,tweetId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                // onSuccess (intentionally left empty)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // onFailure (intentionally left empty)
            }
        })
    }
}