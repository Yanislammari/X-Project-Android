package com.example.x_project_android.view.tweet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.x_project_android.R
import com.example.x_project_android.data.models.Comment
import com.example.x_project_android.event.GlobalEvent
import com.example.x_project_android.event.GlobalEventBus
import com.example.x_project_android.event.SendGlobalEvent
import com.example.x_project_android.view.compose.DisplayLoader
import com.example.x_project_android.viewmodels.tweet.TweetDetailViewModel

@Composable
fun TweetDetailScreen(
    navHostController: NavHostController,
    tweetDetailViewModel: TweetDetailViewModel,
) {
    LaunchedEffect(tweetDetailViewModel.tweetId.value) {
        if (tweetDetailViewModel.tweet.value == null) {
            tweetDetailViewModel.fetchTweet()
        }
        tweetDetailViewModel.fetchComments()
    }

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
        ) {
            item {
                tweetDetailViewModel.tweet.value?.let { tweet ->
                    TweetCell(
                        tweet = tweet,
                        searchValue = "",
                        onClick = {},
                        maxLength = 1000,
                        onPseudoClick = {},
                        onLike = { SendGlobalEvent.onLikeTweet(tweet.id) },
                        onDislike = { SendGlobalEvent.onDislikeTweet(tweet.id) },
                    )
                } ?: Text(
                    stringResource(R.string.tweetdetailscreen_error),
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            item {
                if(tweetDetailViewModel.tweet.value?.isCommented != true) {
                    TextField(
                        value = tweetDetailViewModel.comment.value,
                        onValueChange = { tweetDetailViewModel.setComment(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp, start = 8.dp, end = 8.dp),
                        placeholder = { Text(stringResource(R.string.tweetdetailscreen_comment_placeholder)) },
                        maxLines = 5,
                        singleLine = false,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send Comment",
                                tint = if (tweetDetailViewModel.comment.value.isEmpty()) Color.Gray else MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable(enabled = tweetDetailViewModel.comment.value.isNotEmpty()) {
                                        tweetDetailViewModel.addComment(
                                            tweetDetailViewModel.comment.value,
                                            tweetDetailViewModel.tweetId.value,
                                        )
                                        tweetDetailViewModel.setComment("")
                                    }
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors( unfocusedContainerColor =  MaterialTheme.colorScheme.surface, focusedContainerColor = MaterialTheme.colorScheme.surface ),
                    )
                }
            }
            if(tweetDetailViewModel.isLoadingComment.value) {
                item { DisplayLoader() }
            } else if(tweetDetailViewModel.comments.isEmpty()) {
                item {
                    Text(
                        stringResource(R.string.tweetdetailscreen_no_comment),
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(tweetDetailViewModel.comments.size) { index ->
                    val comment = tweetDetailViewModel.comments[index]
                    CommentCell(
                        comment = comment,
                        onLike = { SendGlobalEvent.onLikeComment(comment.id)},
                        onDislike = { SendGlobalEvent.onDislikeComment(comment.id) },
                    )
                }
            }
        }
    }
}

@Composable
fun CommentCell(
    comment: Comment,
    onLike: () -> Unit,
    onDislike: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        DisplayPseudo(
            comment = comment,
            user = comment.user,
            onClick = {}
        )
        Text(text = comment.content ?: "", style = MaterialTheme.typography.bodyMedium)
        LikesDislikesRow(
            likesCount = comment.likesCount,
            dislikesCount = comment.dislikesCount,
            isLiked = comment.isLiked,
            isDisliked = comment.isDisliked,
            onLike = onLike,
            onDislike = onDislike
        )
    }
}