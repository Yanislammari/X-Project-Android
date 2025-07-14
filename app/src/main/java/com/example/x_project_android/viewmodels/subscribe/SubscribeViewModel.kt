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
import com.example.x_project_android.viewmodels.tweet.imageTest
import kotlinx.coroutines.launch

class SubscribeViewModel : ViewModel() {

    init {
        viewModelScope.launch {
            GlobalEventBus.events.collect { event ->
                onEvent(event)
            }
        }
    }

    private fun onEvent(event: GlobalEvent) {
        when (event) {
            is GlobalEvent.Subscribe -> addWhenSubscribe(event.user)
            is GlobalEvent.Unsubscribe -> deleteWhenUnsubscribe(event.userId)
            else -> {}
        }
    }


    private var _subscriptionsProfile = mutableStateListOf<Tweet>()
    val subscriptionsProfile: List<Tweet> = _subscriptionsProfile

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _hasFetched = mutableStateOf(false)

    suspend fun fetchSubscriptions() {
        if (_hasFetched.value) return
        _isLoading.value = true
        kotlinx.coroutines.delay(500)
        _subscriptionsProfile.addAll(
            listOf(
                Tweet(
                    id = "1",
                    content = "Voici mon chat Miaou !",
                    imageUri = imageTest,
                    user = User(
                        id = "1",
                        pseudo = "Alice",
                        imageUri = imageTest,
                        bio = "J'adore les chats et les chiens !",
                        isSubscribed = true,
                    ),
                    timestamp = System.currentTimeMillis() - 160 * 1000L,
                    likesCount = 10,
                    dislikesCount = 10,
                ),
                Tweet(
                    id = "2",
                    content = "Un tres long texte chiaUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaajUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjnt a afffafhdjsjfjdsjfdkjsjdsjkfkjdskjfdskjkjfdskjfdskjkjfdskjfkjfdjkfskjfkjdsjjfjfjfjfjfjjfjfjfjfjlfdfdjskfslkfd joiezfhouzehfoiezhfoi hezofhzeo fezof hezoih",

                    user = User(
                        id = "2",
                        pseudo = "0123456789012345678901234",
                        imageUri = imageTest,
                        bio = "J'adore lesJ'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens J'adore les chats et les chiens  chats et les chiens !",
                        isSubscribed = true,
                    ),
                    timestamp = System.currentTimeMillis() - 360 * 60 * 1000L,
                    likesCount = 10,
                    dislikesCount = 10,
                    isLiked = true,
                ),
                Tweet(
                    id = "3",
                    content = "Un tres long texte chiant a afffafhdjsjfjdsjfdkjsjdsjkfkjdskjfdskjkjfdskjfdskjkjfdskjfkjfdjkfskjfkjdsjjfjfjfjfjfjjfjfjfjfjlfdfdjskfslkfd joiezfhouzehfoiezhfoi hezofhzeo fezof hezoih",
                    imageUri = imageTest,
                    user = User(
                        id = "3",
                        pseudo = "Exactemenf 25 charachter je le jure enfin je crois",
                        imageUri = imageTest,
                        isSubscribed = true,
                    ),
                    timestamp = System.currentTimeMillis() - 240 * 60 * 60 * 1000L,
                    likesCount = 10,
                    dislikesCount = 10,
                    isDisliked = true,
                )
            )
        )
        _hasFetched.value = true
        _isLoading.value = false
    }

    private fun deleteWhenUnsubscribe(userId: String?){
        if (userId == null) return

        val index = _subscriptionsProfile.indexOfFirst { it.user.id == userId }
        if (index != -1) {
            Log.d("SubscribeViewModel", "Unsubscribed from user with ID: $userId at index $index")
            _subscriptionsProfile.removeAt(index)
        }
    }

    private fun addWhenSubscribe(user: User?) {
        if( user == null) return
        if (_subscriptionsProfile.any { it.user.id == user.id }) return
        _subscriptionsProfile.add(Tweet(
            user = User(
                id = user.id,
                pseudo = user.pseudo,
                imageUri = user.imageUri,
                bio = user.bio,
                isSubscribed = true
            )
        ))
    }
}