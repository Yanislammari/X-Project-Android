package com.example.x_project_android.viewmodels.subscribe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import com.example.x_project_android.event.GlobalEvent
import com.example.x_project_android.event.GlobalEventBus
import com.example.x_project_android.event.NavEvent
import com.example.x_project_android.event.NavEventBus
import com.example.x_project_android.viewmodels.tweet.imageTest

object SubscriptionDetailScreenDest {
    const val ROUTE = "subscription_detail"
    const val USERIDARG = "userId"
    private const val USERIDPLACEHOLDER = "{$USERIDARG}"
    const val FULLROUTE = "$ROUTE/$USERIDPLACEHOLDER"
}

class SubscribeDetailViewModel: ViewModel() {
    private val _userId = mutableStateOf("")
    val userId: State<String> = _userId

    private val  _userDetail = mutableStateOf<User?>(null)
    val userDetail: State<User?> = _userDetail

    private var _tweetProfile = mutableStateListOf<Tweet>()
    val tweetProfile: List<Tweet> = _tweetProfile

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private fun setUser(user: User?) {
        _userDetail.value = user
    }

    fun setUserId(id: String) {
        _userId.value = id
    }

    suspend fun fetchTweetFromUserId(
        userId: String,
    ) {
        _isLoading.value = true
        kotlinx.coroutines.delay(500)
        _tweetProfile.addAll(
            listOf(
                /*Tweet(
                    id = "1",
                    content = "Voici mon chat Miaou !",
                    imageUri = "imageTest",
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
                    imageUri = "imageTest",
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
                )*/
            )
        )
        _isLoading.value = false
    }

    fun statusSubscribe() {
        if(_userDetail.value?.id == null) return
        if(_userDetail.value?.isSubscribed == true){
            setUser(
                _userDetail.value?.copy(isSubscribed = false)
            )
            GlobalEventBus.sendEvent(GlobalEvent.Unsubscribe(_userDetail.value?.id ?: ""))
        }
        else{
            setUser(
                _userDetail.value?.copy(isSubscribed = true)
            )
            GlobalEventBus.sendEvent(GlobalEvent.Subscribe(_userDetail.value?.id ?: ""))
        }
    }

    fun navigateToTweetDetail(tweet: Tweet) {
        NavEventBus.sendEvent(NavEvent.TweetDetail(tweet))
    }
}