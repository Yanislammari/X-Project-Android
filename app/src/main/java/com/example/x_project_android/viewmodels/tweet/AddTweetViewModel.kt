package com.example.x_project_android.viewmodels.tweet

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.event.SendGlobalEvent
import com.example.x_project_android.repositories.TweetRepository
import com.example.x_project_android.utils.byteArrayToRequestBody
import com.example.x_project_android.utils.uriToByteArray
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object AddTweetScreenDest {
    const val ROUTE = "add_tweet"
}

class AddTweetViewModel(
    private val tweetRepository: TweetRepository = TweetRepository()
):ViewModel(){
    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> get() = _imageUri
    fun setImageUri(uri: Uri?) {
        if(uri == null || uri.toString().isEmpty()) {
           return
        }
        _imageUri.value = uri
    }

    private val _state = mutableStateOf(AddTweetState.Initial)
    val state: State<AddTweetState> = _state

    private val _uiEvent = Channel<AddUiTweetEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _content = mutableStateOf("")
    val content: State<String> get() = _content
    fun setContent(newContent: String) {
        if (newContent.length <= 1000) {
            _content.value = newContent
        }
    }

    private fun clearAddTweet() {
        _imageUri.value = null
        _content.value = ""
    }

    fun addTweet(context : Context){
        if (_content.value.isBlank()) {
            _uiEvent.trySend(AddUiTweetEvent.Error("Content cannot be empty"))
            return
        }
        else if (_imageUri.value == null) {
            _uiEvent.trySend(AddUiTweetEvent.Error("Image is required"))
            return
        }
        val bytes = uriToByteArray(context, _imageUri.value ?: return)
        if(bytes == null){
            _uiEvent.trySend(AddUiTweetEvent.Error("An error occurred while processing the image. Please try again."))
            return
        }
        val tweetPicture = byteArrayToRequestBody(bytes, "image/jpeg")
        _state.value = AddTweetState.Loading
        tweetRepository.postTweets(_content.value,tweetPicture){result ->
            when(result) {
                is AddTweetResult.Success -> {
                    _state.value = AddTweetState.Success
                    SendGlobalEvent.onAddTweet(result.tweet)
                    _uiEvent.trySend(AddUiTweetEvent.Success(result.message))
                    clearAddTweet()
                }
                is AddTweetResult.Failure -> {
                    _state.value = AddTweetState.Error
                    _uiEvent.trySend(AddUiTweetEvent.Error(result.message))
                }
            }
        }


    }
}

sealed class AddUiTweetEvent {
    data class Success(val message: String) : AddUiTweetEvent()
    data class Error(val message: String) : AddUiTweetEvent()
}

sealed class AddTweetResult {
    data class Success(val message: String,val tweet:Tweet) : AddTweetResult()
    data class Failure(val message: String) : AddTweetResult()
}

enum class AddTweetState {
    Initial,
    Loading,
    Success,
    Error
}