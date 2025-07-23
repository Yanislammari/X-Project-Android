package com.example.x_project_android.viewmodels.subscribe

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import com.example.x_project_android.event.GlobalEvent
import com.example.x_project_android.event.GlobalEventBus
import com.example.x_project_android.repositories.TweetRepository
import com.example.x_project_android.tokenStore.TokenManager
import com.example.x_project_android.viewmodels.tweet.AddTweetState
import com.example.x_project_android.viewmodels.tweet.AddUiTweetEvent
import com.example.x_project_android.viewmodels.tweet.imageTest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SubscribeViewModel(
    private val tweetRepository: TweetRepository = TweetRepository()
): ViewModel() {

    init {
        viewModelScope.launch {
            GlobalEventBus.events.collect { event ->
                onEvent(event)
            }
        }
    }

    private fun onEvent(event: GlobalEvent) {
        when (event) {
            is GlobalEvent.Subscribe -> addWhenSubscribe(event.tweet)
            is GlobalEvent.Unsubscribe -> deleteWhenUnsubscribe(event.userId)
            else -> {}
        }
    }


    private var _subscriptionsProfile = mutableStateListOf<Tweet>()
    val subscriptionsProfile: List<Tweet> = _subscriptionsProfile

    private val _state = mutableStateOf(SubscribeState.Initial)
    val state: State<SubscribeState> = _state

    fun fetchSubscriptions() {
        if(_state.value == SubscribeState.Success) return
        _state.value = SubscribeState.Loading
        tweetRepository.getAllSub{ result ->
            when (result) {
                is SubscribeResult.Success -> {
                    _subscriptionsProfile.addAll(result.tweets)
                    _state.value = SubscribeState.Success
                }
                is SubscribeResult.Failure -> {
                    _state.value = SubscribeState.Error
                }
            }

        }
    }

    private fun deleteWhenUnsubscribe(userId: String?){
        if (userId == null) return

        val index = _subscriptionsProfile.indexOfFirst { it.user.id == userId }
        if (index != -1) {
            Log.d("SubscribeViewModel", "Unsubscribed from user with ID: $userId at index $index")
            _subscriptionsProfile.removeAt(index)
        }
    }

    private fun addWhenSubscribe(tweet: Tweet?) {
        if( tweet == null) return
        if (_subscriptionsProfile.any { it.user.id == tweet.user.id }) return
        _subscriptionsProfile.add(tweet)
    }
}

sealed class SubscribeResult {
    data class Success(val message: String,val tweets:List<Tweet>) : SubscribeResult()
    data class Failure(val message: String) : SubscribeResult()
}

enum class SubscribeState {
    Initial,
    Loading,
    Success,
    Error
}