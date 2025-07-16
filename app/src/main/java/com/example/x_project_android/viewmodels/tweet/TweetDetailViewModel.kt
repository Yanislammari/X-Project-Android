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
import kotlinx.coroutines.*

object TweetDetailScreenDest {
    const val ROUTE = "tweet_detail"
    const val TWEETIDARG = "tweetId"
    private const val TWEETIDPLACEHOLDER = "{$TWEETIDARG}"
    const val FULLROUTE = "$ROUTE/$TWEETIDPLACEHOLDER?origin={origin}"
}

class TweetDetailViewModel : ViewModel() {

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

    private val _hasFetchedComments = mutableStateOf(false)

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isLoadingComment = mutableStateOf(false)
    val isLoadingComment: State<Boolean> = _isLoadingComment

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

    suspend fun fetchComments(tweetId: String? = _tweetId.value) {
        if (tweetId == null) return
        if (_hasFetchedComments.value) return
        _isLoadingComment.value = true
        delay(1000)
        _comments.clear()
        _comments.addAll(
            listOf(
                Comment(
                    id = "comment_1",
                    tweetId = _tweetId.value,
                    content = "C'est trop mignon !",
                    user = User(
                        id = "2",
                        pseudo = "Bob",
                        imageUri = imageTest,
                    ),
                    timestamp = System.currentTimeMillis() - 100 * 1000L,
                    likesCount = 5,
                    dislikesCount = 0,
                ),
                Comment(
                    id = "comment_2",
                    tweetId = _tweetId.value,
                    content = "J'adore les chats !",
                    user = User(
                        id = "3",
                        pseudo = "Charlie",
                        imageUri = imageTest,
                    ),
                    timestamp = System.currentTimeMillis() - 50 * 1000L,
                    likesCount = 3,
                    dislikesCount = 1,
                ),
                Comment(
                    id = "comment_3",
                    tweetId = _tweetId.value,
                    content = "Alice sub !",
                    user = User(
                        id = "1",
                        pseudo = "Alice",
                        imageUri = imageTest,
                    ),
                    timestamp = System.currentTimeMillis() - 50 * 1000L,
                    likesCount = 3,
                    dislikesCount = 1,
                )
            )
        )
        _hasFetchedComments.value = true
        _isLoadingComment.value = false
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

    fun addComment(){
        val comment = Comment(
            id = "random_"+System.currentTimeMillis(),
            tweetId = _tweetId.value,
            content = _comment.value,
            user = _tweet.value?.user!!,
            timestamp = System.currentTimeMillis(),
        )
        SendGlobalEvent.onAddComment(_tweetId.value, comment)
        setComment("")
    }
}