package com.example.x_project_android.viewmodels.tweet

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
import com.example.x_project_android.repositories.TweetRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object TweetDetailScreenDest {
    const val ROUTE = "tweet_detail"
    const val TWEETIDARG = "tweetId"
    private const val TWEETIDPLACEHOLDER = "{$TWEETIDARG}"
    const val FULLROUTE = "$ROUTE/$TWEETIDPLACEHOLDER?origin={origin}"
}

class TweetDetailViewModel(
    private val tweetRepository: TweetRepository = TweetRepository()
) : ViewModel() {

    init {
        viewModelScope.launch {
            GlobalEventBus.events.collect { event ->
                onGlobalEvent(event)
            }
        }
    }

    private fun onGlobalEvent(event: GlobalEvent) {
        when (event) {
            is GlobalEvent.Like -> likeTweet(event.tweetId)
            is GlobalEvent.Dislike -> dislikeTweet(event.tweetId)
            is GlobalEvent.LikeComment -> likeComment(event.commentId)
            is GlobalEvent.DislikeComment -> dislikeComment(event.commentId)
            is GlobalEvent.AddComment -> addComment(event.comment)
            else -> {}
        }
    }

    private val _tweetId = mutableStateOf("")
    val tweetId: State<String> = _tweetId

    private val _tweet = mutableStateOf<Tweet?>(null)
    val tweet: State<Tweet?> = _tweet

    private var _comments = mutableStateListOf<Comment>()
    val comments: List<Comment> = _comments

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _state = mutableStateOf(CommentState.Initial)
    val state: State<CommentState> = _state

    private val _uiEvent = Channel<CommentUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _comment = mutableStateOf("")
    val comment: State<String> = _comment

    fun setTweetId(id: String) {
        _tweetId.value = id
    }

    fun setTweet(tweet: Tweet?) {
        _tweet.value = tweet
    }

    fun setComment(newComment: String) {
        if (newComment.length < 100) {
            _comment.value = newComment
        }
    }

    suspend fun fetchTweet() {
        _isLoading.value = true
        Tweet(
            id = _tweetId.value,
            content = "Voici mon chat Miaou !",
            imageUri = imageTest,
            user = User(
                id = "1",
                pseudo = "Alice",
                imageUri = imageTest,
            ),
            timestamp = System.currentTimeMillis() - 160 * 1000L,
            likesCount = 15,
            dislikesCount = 10,
        )
        delay(1000)
        _isLoading.value = false
    }

    fun fetchComments(tweetId: String? = _tweetId.value) {
        if(tweetId == null){
            _uiEvent.trySend(CommentUiEvent.Error("An error occurred: No tweet ID found."))
            return
        }
        if(_state.value == CommentState.Success) return
        _state.value = CommentState.Loading
        tweetRepository.getComments(tweetId){result ->
            when (result) {
                is CommentResult.Success -> {
                    _comments.clear()
                    _comments.addAll(result.comments)
                    _state.value = CommentState.Success
                }
                is CommentResult.Failure -> {
                    _state.value = CommentState.Error
                }
            }
        }
    }

    private fun addComment(comment: Comment?) {
        if (comment == null) return
        if (comment.tweetId != _tweetId.value) return
        _comments.add(comment)
        if (_tweet.value?.id != comment.tweetId) return
        setTweet(
            tweet.value?.copy(
                isCommented = true
            )
        )
    }

    private fun likeTweet(tweetId: String?) {
        tweetId ?: return
        val oldTweet = _tweet.value ?: return
        if (oldTweet.id != tweetId) return

        val wasDisliked = oldTweet.isDisliked
        val wasLiked = oldTweet.isLiked

        val updatedTweet = oldTweet.copy(
            isDisliked = false,
            dislikesCount = if (wasDisliked) maxOf(
                0,
                oldTweet.dislikesCount - 1
            ) else oldTweet.dislikesCount,
            isLiked = !wasLiked,
            likesCount = if (wasLiked) maxOf(
                0,
                oldTweet.likesCount - 1
            ) else oldTweet.likesCount + 1
        )

        setTweet(updatedTweet)
    }

    private fun dislikeTweet(tweetId: String?) {
        tweetId ?: return
        val oldTweet = _tweet.value ?: return
        if (oldTweet.id != tweetId) return

        val wasDisliked = oldTweet.isDisliked
        val wasLiked = oldTweet.isLiked

        val updatedTweet = oldTweet.copy(
            isDisliked = !wasDisliked,
            dislikesCount = if (wasDisliked) maxOf(
                0,
                oldTweet.dislikesCount - 1
            ) else oldTweet.dislikesCount + 1,
            isLiked = false,
            likesCount = if (wasLiked) maxOf(0, oldTweet.likesCount - 1) else oldTweet.likesCount
        )

        setTweet(updatedTweet)
    }

    private fun likeComment(commentId: String?) {
        commentId ?: return
        val index = _comments.indexOfFirst { it.id == commentId }
        if (index == -1) return

        val oldComment = _comments[index]
        val wasDisliked = oldComment.isDisliked
        val wasLiked = oldComment.isLiked

        val updatedComment = oldComment.copy(
            isDisliked = false,
            dislikesCount = if (wasDisliked) maxOf(
                0,
                oldComment.dislikesCount - 1
            ) else oldComment.dislikesCount,
            isLiked = !wasLiked,
            likesCount = if (wasLiked) maxOf(
                0,
                oldComment.likesCount - 1
            ) else oldComment.likesCount + 1
        )

        _comments[index] = updatedComment
    }

    private fun dislikeComment(commentId: String?) {
        commentId ?: return
        val index = _comments.indexOfFirst { it.id == commentId }
        if (index == -1) return

        val oldComment = _comments[index]
        val wasDisliked = oldComment.isDisliked
        val wasLiked = oldComment.isLiked

        val updatedComment = oldComment.copy(
            isDisliked = !wasDisliked,
            dislikesCount = if (wasDisliked) maxOf(
                0,
                oldComment.dislikesCount - 1
            ) else oldComment.dislikesCount + 1,
            isLiked = false,
            likesCount = if (wasLiked) maxOf(
                0,
                oldComment.likesCount - 1
            ) else oldComment.likesCount
        )

        _comments[index] = updatedComment
    }

    fun addComment(tweetId: String? = _tweetId.value){
        if(tweetId == null){
            _uiEvent.trySend(CommentUiEvent.Error("An error occurred: No tweet ID found."))
            return
        }
        tweetRepository.postComment(tweetId,_comment.value){result->
            when(result){
                is AddCommentResult.Success -> {
                    _uiEvent.trySend(CommentUiEvent.Success(result.message))
                    SendGlobalEvent.onAddComment(_tweetId.value, result.comment)
                    setComment("")
                }
                is AddCommentResult.Failure -> {
                    _uiEvent.trySend(CommentUiEvent.Error(result.message))
                }
            }
        }

    }
}

sealed class CommentUiEvent {
    data class Success(val message: String) : CommentUiEvent()
    data class Error(val message: String) : CommentUiEvent()
}

sealed class AddCommentResult {
    data class Success(val message: String,val comment:Comment) : AddCommentResult()
    data class Failure(val message: String) : AddCommentResult()
}

sealed class CommentResult {
    data class Success(val message: String, val comments:List<Comment>) : CommentResult()
    data class Failure(val message: String) : CommentResult()
}

enum class CommentState {
    Initial,
    Loading,
    Success,
    Error
}