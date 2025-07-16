package com.example.x_project_android.viewmodels.tweet

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import com.example.x_project_android.event.SendGlobalEvent

object AddTweetScreenDest {
    const val ROUTE = "add_tweet"
}

class AddTweetViewModel :ViewModel(){
    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> get() = _imageUri
    fun setImageUri(uri: Uri?) {
        if(uri == null || uri.toString().isEmpty()) {
           return
        }
        _imageUri.value = uri
    }

    private val _content = mutableStateOf("")
    val content: State<String> get() = _content
    fun setContent(newContent: String) {
        if (newContent.length <= 1000) {
            _content.value = newContent
        }
    }

    fun clearAddTweet() {
        _imageUri.value = null
        _content.value = ""
    }

    fun addTweet(){
        val tweet = Tweet(
            id = "just_a_random_id",
            content = _content.value,
            imageUri = _imageUri.value?.toString(),
            user = User(
                id = "user_id",
                pseudo = "User Name",
                bio = "username",
                imageUri = _imageUri.value?.toString()
            ),
        )
        SendGlobalEvent.onAddTweet(tweet)
        clearAddTweet()
    }
}