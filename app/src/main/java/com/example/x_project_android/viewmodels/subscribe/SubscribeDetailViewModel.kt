package com.example.x_project_android.viewmodels.subscribe

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.x_project_android.data.models.Comment
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import com.example.x_project_android.event.GlobalEvent
import com.example.x_project_android.event.GlobalEventBus
import com.example.x_project_android.event.SendGlobalEvent
import com.example.x_project_android.viewmodels.tweet.imageTest
import kotlinx.coroutines.launch

object SubscriptionDetailScreenDest {
    const val ROUTE = "subscription_detail"
    const val USERIDARG = "userId"
    private const val USERIDPLACEHOLDER = "{$USERIDARG}"
    const val FULLROUTE = "$ROUTE/$USERIDPLACEHOLDER"
}

class SubscribeDetailViewModel: ViewModel() {

    init {
        viewModelScope.launch {
            GlobalEventBus.events.collect { event ->
                onEvent(event)
            }
        }
    }

    private fun onEvent(event: GlobalEvent) {
        when (event) {
            is GlobalEvent.Like -> likeTweet(event.tweetId)
            is GlobalEvent.Dislike -> dislikeTweet(event.tweetId)
            is GlobalEvent.Unsubscribe -> statusSubscribe(event.userId,false)
            is GlobalEvent.Subscribe -> statusSubscribe(event.user.id,true)
            else -> {}
        }
    }

    private val _userId = mutableStateOf("")
    val userId: State<String> = _userId

    private val  _userDetail = mutableStateOf<User?>(null)
    val userDetail: State<User?> = _userDetail

    private var _tweetProfile = mutableStateListOf<Tweet>()
    val tweetProfile: List<Tweet> = _tweetProfile

    private val _hasFetchedTweets = mutableStateOf(false)

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private fun setUser(user: User?) {
        _userDetail.value = user
    }

    fun setUserId(id: String) {
        _userId.value = id
    }

    fun setUserDetail(user: User?) {
        setUser(user)
    }

    suspend fun fetchTweetFromUserId(
        userId: String,
    ) {
        if(_hasFetchedTweets.value)return
        _isLoading.value = true
        kotlinx.coroutines.delay(500)
        _tweetProfile.addAll(
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
                    content = "Un tres long texte chiant a afffafhdjsjfjdsj...",
                    imageUri = imageTest,
                    user = User(
                        id = "1",
                        pseudo = "Alice",
                        imageUri = imageTest,
                        bio = "J'adore les chats et les chiens !",
                        isSubscribed = true,
                    ),
                    timestamp = System.currentTimeMillis() - 360 * 60 * 1000L,
                    likesCount = 10,
                    dislikesCount = 10,
                )
            )
        )
        _hasFetchedTweets.value = true
        _isLoading.value = false
    }

    private fun statusSubscribe(userId : String?,state : Boolean) {
        if(userId == null || _userDetail.value?.id != userId) return
        setUserDetail(
            _userDetail.value?.copy(
                isSubscribed = state
            )
        )
    }

    fun sendEventStateSub(){
        if(_userDetail.value == null) return
        if(_userDetail.value?.isSubscribed == true) {
            SendGlobalEvent.onUnsubscribe(userId.value)
        } else {
            SendGlobalEvent.onSubscribe(_userDetail.value)
        }
    }

    private fun likeTweet(tweetId: String?){
        tweetId ?: return
        val index = _tweetProfile.indexOfFirst { it.id == tweetId }
        if (index == -1) return

        val oldTweet = _tweetProfile[index]
        val wasDisliked = oldTweet.isDisliked
        val wasLiked = oldTweet.isLiked

        val updatedTweet = oldTweet.copy(
            isDisliked = false,
            dislikesCount = if (wasDisliked) maxOf(0, oldTweet.dislikesCount - 1) else oldTweet.dislikesCount,
            isLiked = !wasLiked,
            likesCount = if (wasLiked) maxOf(0, oldTweet.likesCount - 1) else oldTweet.likesCount + 1
        )
        _tweetProfile[index] = updatedTweet
    }

    private fun dislikeTweet(tweetId: String?){
        tweetId ?: return
        val index = _tweetProfile.indexOfFirst { it.id == tweetId }
        if (index == -1) return

        val oldTweet = _tweetProfile[index]
        val wasDisliked = oldTweet.isDisliked
        val wasLiked = oldTweet.isLiked

        val updatedTweet = oldTweet.copy(
            isDisliked = !wasDisliked,
            dislikesCount = if (wasDisliked) maxOf(0, oldTweet.dislikesCount - 1) else oldTweet.dislikesCount + 1,
            isLiked = false,
            likesCount = if (wasLiked) maxOf(0, oldTweet.likesCount - 1) else oldTweet.likesCount
        )
        _tweetProfile[index] = updatedTweet
    }
}