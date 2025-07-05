package com.example.x_project_android.viewmodels.tweet

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.x_project_android.data.models.Comment
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import kotlinx.coroutines.*


object TweetDetailScreenDest {
    private const val ROUTE = "tweet_detail"
    const val TWEETIDARG = "tweetId"
    private const val TWEETIDPLACEHOLDER = "{$TWEETIDARG}"
    const val FULLROUTE = "$ROUTE/$TWEETIDPLACEHOLDER"
}

class TweetDetailViewModel: ViewModel() {
    private val _tweetId = mutableStateOf("")
    val tweetId: State<String> = _tweetId

    private var _comments = mutableStateListOf<Comment>()
    val comments: List<Comment> = _comments

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isLoadingComment = mutableStateOf(false)
    val isLoadingComment: State<Boolean> = _isLoadingComment

    private val _comment = mutableStateOf("")
    val comment: State<String> = _comment

    fun setTweetId(id: String) {
        _tweetId.value = id
    }

    fun setComment(newComment: String) {
        if(newComment.length < 100) {
            _comment.value = newComment
        }
    }

    suspend fun fetchTweet(sharedViewModel: SharedTweetViewModel) {
        _isLoading.value = true
        val a = Tweet(
            id = _tweetId.value,
            content = "Voici mon chat Miaou !",
            imageUri = imageTest,
            user = User(
                id = "1",
                pseudo = "Alice",
                imageUri = imageTest,
            ),
            timestamp = System.currentTimeMillis() - 160 * 1000L,
            likesCount = 10,
            dislikesCount = 10,
        )
        delay(1000)
        setTweetInShared(a,sharedViewModel)
        _isLoading.value = false
    }

    suspend fun fetchComments(tweetId:String? = _tweetId.value) {
        if (tweetId == null) return
        _isLoadingComment.value = true
        delay(1000)
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
                )
            )
        )
        _isLoadingComment.value = false
    }

    private fun setTweetInShared(tweet: Tweet?, viewModel: SharedTweetViewModel) {
        viewModel.setTweet(tweet)
    }

    fun addComment(sharedViewModel: SharedTweetViewModel,tweetsViewModel: TweetsViewModel,comment : String, tweetId: String) {
        val commentA = Comment(
            id = "comment_${System.currentTimeMillis()}",
            tweetId = tweetId,
            content = comment,
            user = User(
                id = "1",
                pseudo = "Alice",
                imageUri = imageTest,
            ),
            timestamp = System.currentTimeMillis(),
        )
        _comments.add(commentA)
        sharedViewModel.markTweetAsCommented()
        tweetsViewModel.markTweetAsCommented(tweetId)
    }

    fun likeComment(commentId: String?){
        commentId ?: return
        val index = _comments.indexOfFirst { it.id == commentId }
        if (index == -1) return

        val oldComment = _comments[index]
        val wasDisliked = oldComment.isDisliked
        val wasLiked = oldComment.isLiked

        val updatedComment = oldComment.copy(
            isDisliked = false,
            dislikesCount = if (wasDisliked) maxOf(0, oldComment.dislikesCount - 1) else oldComment.dislikesCount,
            isLiked = !wasLiked,
            likesCount = if (wasLiked) maxOf(0, oldComment.likesCount - 1) else oldComment.likesCount + 1
        )

        _comments[index] = updatedComment
    }

    fun dislikeComment(commentId: String?){
        commentId ?: return
        val index = _comments.indexOfFirst { it.id == commentId }
        if (index == -1) return

        val oldComment = _comments[index]
        val wasDisliked = oldComment.isDisliked
        val wasLiked = oldComment.isLiked

        val updatedComment = oldComment.copy(
            isDisliked = !wasDisliked,
            dislikesCount = if (wasDisliked) maxOf(0, oldComment.dislikesCount - 1) else oldComment.dislikesCount + 1,
            isLiked = false,
            likesCount = if (wasLiked) maxOf(0, oldComment.likesCount - 1) else oldComment.likesCount
        )

        _comments[index] = updatedComment
    }
}