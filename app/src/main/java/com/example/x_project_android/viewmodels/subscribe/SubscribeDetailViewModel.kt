package com.example.x_project_android.viewmodels.subscribe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import com.example.x_project_android.event.GlobalEvent
import com.example.x_project_android.event.GlobalEventBus
import com.example.x_project_android.event.SendGlobalEvent
import com.example.x_project_android.repositories.LikeDislikeRepository
import com.example.x_project_android.repositories.TweetRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object SubscriptionDetailScreenDest {
    const val ROUTE = "subscription_detail"
    const val USERIDARG = "userId"
    private const val USERIDPLACEHOLDER = "{$USERIDARG}"
    const val FULLROUTE = "$ROUTE/$USERIDPLACEHOLDER"
}

class SubscribeDetailViewModel(
    private val tweetRepository: TweetRepository = TweetRepository(),
    private val likeDislikeRepository: LikeDislikeRepository = LikeDislikeRepository(),
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
            is GlobalEvent.Like -> likeTweet(event.tweetId)
            is GlobalEvent.Dislike -> dislikeTweet(event.tweetId)
            is GlobalEvent.Unsubscribe -> statusSubscribe(event.userId,false)
            is GlobalEvent.Subscribe -> statusSubscribe(event.tweet.user.id,true)
            else -> {}
        }
    }

    private val _userId = mutableStateOf("")
    val userId: State<String> = _userId

    private val  _userDetail = mutableStateOf<User?>(null)
    val userDetail: State<User?> = _userDetail

    private var _tweetProfile = mutableStateListOf<Tweet>()
    val tweetProfile: List<Tweet> = _tweetProfile

    private val _state = mutableStateOf(SubscribeDetailState.Initial)
    val state: State<SubscribeDetailState> = _state

    private val _stateOfSub = mutableStateOf(SubscribeToState.Initial)
    val stateOfSub: State<SubscribeToState> = _stateOfSub

    private val _uiEvent = Channel<SubscribeDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun setUser(user: User?) {
        _userDetail.value = user
    }

    fun setUserId(id: String) {
        _userId.value = id
    }

    fun setUserDetail(user: User?) {
        setUser(user)
    }

    fun fetchTweetFromUserId(
        userId: String,
    ) {
       if(_state.value == SubscribeDetailState.Success)return
        _state.value = SubscribeDetailState.Loading
       tweetRepository.getTweetsByUserId(userId){result->
           when(result){
               is SubscribeDetailResult.Success -> {
                   _state.value = SubscribeDetailState.Success
                   _tweetProfile.clear()
                   _tweetProfile.addAll(result.tweets)
               }
               is SubscribeDetailResult.Failure -> {
                   _state.value = SubscribeDetailState.Error
               }
               else -> {
                   _state.value = SubscribeDetailState.Error
               }
           }
       }
    }

    private fun statusSubscribe(userId : String?,state : Boolean) {
        if(userId == null || _userDetail.value?.id != userId) return
        setUserDetail(
            _userDetail.value?.copy(
                isSubscribed = state
            )
        )
    }

    suspend fun sendEventStateSub(){
        if(_userId.value == "" || _userDetail.value == null) return
        _stateOfSub.value = SubscribeToState.Loading
        delay(1000)
        if(_userDetail.value?.isSubscribed == false) {
            tweetRepository.getTweetWhenSub(_userId.value){ result ->
                when(result){
                    is SubscribeDetailResult.SuccessSub -> {
                        _stateOfSub.value = SubscribeToState.Success
                        SendGlobalEvent.onSubscribe(result.tweet)
                        _uiEvent.trySend(SubscribeDetailUiEvent.Success("Subscribed successfully"))
                    }
                    else ->{
                        _stateOfSub.value = SubscribeToState.Error
                        _uiEvent.trySend(SubscribeDetailUiEvent.Error("An error occurred while subscribing"))
                        setUserDetail(
                            _userDetail.value?.copy(
                                isSubscribed = false
                            )
                        )
                    }
                }
            }
        }
        else{
            tweetRepository.postUnsub(_userId.value){ result ->
                when(result){
                    is SubscribeDetailResult.SuccessUnsub -> {
                        _stateOfSub.value = SubscribeToState.Success
                        SendGlobalEvent.onUnsubscribe(userId.value)
                        _uiEvent.trySend(SubscribeDetailUiEvent.Success("Unsubscribed successfully"))
                    }
                    else ->{
                        _stateOfSub.value = SubscribeToState.Error
                        _uiEvent.trySend(SubscribeDetailUiEvent.Error("An error occurred while unsubscribing"))
                        setUserDetail(
                            _userDetail.value?.copy(
                                isSubscribed = true
                            )
                        )
                    }
                }
            }
        }
        SubscribeToState.Initial
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

    fun sendLikeTweet(tweetId : String?){
        if(tweetId == null) return
        likeDislikeRepository.likeTweet(tweetId)
        SendGlobalEvent.onLikeTweet(tweetId)
    }
    fun sendDislikeTweet(tweetId : String?){
        if(tweetId == null) return
        likeDislikeRepository.dislikeTweet(tweetId)
        SendGlobalEvent.onDislikeTweet(tweetId)
    }
}

sealed class SubscribeDetailUiEvent {
    data class Success(val message: String) : SubscribeDetailUiEvent()
    data class Error(val message: String) : SubscribeDetailUiEvent()
}

sealed class SubscribeDetailResult {
    data class Success(val message: String,val tweets:List<Tweet>) : SubscribeDetailResult()
    data class SuccessSub(val message: String, val tweet: Tweet) : SubscribeDetailResult()
    data class SuccessUnsub(val message: String) : SubscribeDetailResult()
    data class Failure(val message: String) : SubscribeDetailResult()
}

enum class SubscribeDetailState {
    Initial,
    Loading,
    Success,
    Error
}

enum class SubscribeToState {
    Initial,
    Loading,
    Success,
    Error
}