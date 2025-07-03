package com.example.x_project_android.view.tweet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.x_project_android.R
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import com.example.x_project_android.utils.getRelativeTime
import com.example.x_project_android.utils.reduceText
import com.example.x_project_android.view.compose.DisplayLoader
import com.example.x_project_android.view.compose.buildHighlightedText
import com.example.x_project_android.viewmodels.tweet.TweetsViewModel

@Composable
fun TweetScreen(
    navHostController: NavHostController,
    tweetsViewModel: TweetsViewModel
) {
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        tweetsViewModel.fetchTweets()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
    ) {
        SimpleSearchBar(
            query = tweetsViewModel.searchText.value,
            onQueryChange = { tweetsViewModel.setSearchText(it) },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        )

        if (tweetsViewModel.isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                DisplayLoader()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f) // make this take remaining height
            ) {
                items(tweetsViewModel.getTweets()) { tweet ->
                    TweetCell(
                        tweet = tweet,
                        onLike = {
                            focusManager.clearFocus()
                            tweetsViewModel.likeTweet(tweet.id ?: "")
                        },
                        onDislike = {
                            focusManager.clearFocus()
                            tweetsViewModel.dislikeTweet(tweet.id ?: "")
                        },
                        tweetsViewModel = tweetsViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun TweetCell(
    tweet: Tweet,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    tweetsViewModel: TweetsViewModel
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clip(RoundedCornerShape(16.dp))  // round corners here
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Put image at the top if exists
        if (tweet.imageUri != null) {
            AsyncImage(
                model = tweet.imageUri,
                contentDescription = stringResource(R.string.tweetscreen_description_tweet_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 300.dp)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0)) // light gray background behind text & user info
                .padding(8.dp)
        ) {
            DisplayPseudo(tweet, tweet.user)
            Text(
                text = buildHighlightedText(tweet.content, tweetsViewModel.searchText.value, MaterialTheme.colorScheme.primary),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp,top = 4.dp),
                overflow = TextOverflow.Ellipsis
            )
            LikesDislikesRow(tweet, onLike, onDislike)
        }
    }
}

@Composable
fun DisplayPseudo(
    tweet: Tweet,
    user: User,
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = user.imageUri,
            contentDescription = stringResource(R.string.tweetscreen_image_content_desc),
            modifier = Modifier
                .size(25.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = reduceText(user.pseudo),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))  // <-- This spacer pushes timestamp right
        Text(
            text = tweet.timestamp?.let { getRelativeTime(it) } ?: stringResource(R.string.tweetscreen_texttime_error),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}


@Composable
fun LikesDislikesRow(
    tweet: Tweet,
    onLike: () -> Unit,
    onDislike: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Like button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    if (tweet.isLiked) Color(0xFFE0F2F1) else Color(0xFFF0F0F0)
                )
                .clickable(onClick = onLike)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = stringResource(R.string.tweetscreen_text_like),
                tint = if (tweet.isLiked) Color(0xFF26A69A) else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "${tweet.likesCount}",
                color = if (tweet.isLiked) Color(0xFF26A69A) else Color.Black,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }

        // Dislike button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    if (tweet.isDisliked) Color(0xFFFFEBEE) else Color(0xFFF0F0F0)
                )
                .clickable(onClick = onDislike)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = stringResource(R.string.tweetscreen_text_dislike),
                tint = if (tweet.isDisliked) Color(0xFFEF5350) else Color.Gray,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(180f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "${tweet.dislikesCount}",
                color = if (tweet.isDisliked) Color(0xFFEF5350) else Color.Black,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun SimpleSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            .heightIn(min = 56.dp),
        placeholder = { Text(stringResource(R.string.tweetscreen_placeholder_search)) },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        )
    )
}



@Preview
@Composable
fun SimpleSearchBarPreview() {
    SimpleSearchBar(
        query = "Search",
        onQueryChange = {}
    )
}