package com.example.x_project_android.view.tweet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.x_project_android.R
import com.example.x_project_android.data.models.Comment
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import com.example.x_project_android.event.SendGlobalEvent
import com.example.x_project_android.event.SendNavEvent
import com.example.x_project_android.utils.getRelativeTime
import com.example.x_project_android.utils.reduceText
import com.example.x_project_android.view.compose.DisplayLoader
import com.example.x_project_android.view.compose.DividerHorizontal
import com.example.x_project_android.view.compose.buildHighlightedText
import com.example.x_project_android.viewmodels.tweet.TweetDetailScreenDest
import com.example.x_project_android.viewmodels.tweet.TweetsViewModel

@Composable
fun TweetScreen(
    navHostController: NavHostController,
    tweetsViewModel: TweetsViewModel,
) {
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit){
        tweetsViewModel.fetchTweets()
    }

    Column(
        modifier = Modifier
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
                .padding(8.dp),
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
                    .weight(1f)
            ) {
                items(tweetsViewModel.getTweetsBySearchValue()) { tweet ->
                    TweetCell(
                        tweet = tweet,
                        searchValue = tweetsViewModel.searchText.value,
                        onClick = {
                            focusManager.clearFocus()
                            SendNavEvent.onTweetDetail(tweet)
                            navHostController.navigate(TweetDetailScreenDest.ROUTE+"/${tweet.id}")
                        },
                        imageHeight = 150,
                        onPseudoClick = {
                            focusManager.clearFocus()
                            SendNavEvent.onSubscribeDetail(tweet.user)
                            navHostController.navigate("subscription_detail/${tweet.user.id}")
                        },
                        onLike = {SendGlobalEvent.onLikeTweet(tweet.id)},
                        onDislike = {SendGlobalEvent.onDislikeTweet(tweet.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TweetCell(
    tweet: Tweet,
    searchValue: String,
    onClick: () -> Unit,
    maxLength: Int = 350,
    imageHeight: Int? = null,
    onPseudoClick: () -> Unit,
    onLike: () -> Unit = {},
    onDislike: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                onClick = onClick,
            )
    ) {
        if (tweet.imageUri != null) {
            AsyncImage(
                model = tweet.imageUri,
                contentDescription = stringResource(R.string.tweetscreen_description_tweet_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (imageHeight != null) Modifier.height(imageHeight.dp) else Modifier
                    )
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )
        }
        DisplayPseudo(tweet = tweet, user = tweet.user, onClick = onPseudoClick)
        Text(
            text = buildHighlightedText(
                tweet.content,
                searchValue,
                MaterialTheme.colorScheme.primary,
                maxLength
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp, bottom = 16.dp, top = 4.dp),
            overflow = TextOverflow.Ellipsis
        )
        LikesDislikesRow(
            tweet.isLiked,
            tweet.likesCount,
            tweet.isDisliked,
            tweet.dislikesCount,
            onLike,
            onDislike,
        )
        DividerHorizontal()
    }
}

@Composable
fun DisplayPseudo(
    formatedPseudo: String? = null,
    tweet: Tweet? = null,
    comment: Comment? = null,
    user: User,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
            ),
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
            text = reduceText(formatedPseudo ?: user.pseudo),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        if (tweet != null) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = tweet.timestamp?.let { getRelativeTime(it) }
                    ?: stringResource(R.string.tweetscreen_texttime_error),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        } else if (comment != null) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = comment.timestamp?.let { getRelativeTime(it) }
                    ?: stringResource(R.string.tweetscreen_texttime_error),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun LikesDislikesRow(
    isLiked: Boolean,
    likesCount: Int,
    isDisliked: Boolean,
    dislikesCount: Int,
    onLike: () -> Unit,
    onDislike: () -> Unit
) {
    val interactionSourceLike = remember { MutableInteractionSource() }
    val interactionSourceDislike = remember { MutableInteractionSource() }
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
                .background(MaterialTheme.colorScheme.surface)
                .selectable(
                    selected = false,
                    onClick = onLike,
                    indication = ripple(
                        radius = 65.dp,
                        color = Color(0xFF26A69A)
                    ),
                    interactionSource = interactionSourceLike
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = stringResource(R.string.tweetscreen_text_like),
                tint = if (isLiked) Color(0xFF26A69A) else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$likesCount",
                color = if (isLiked) Color(0xFF26A69A) else Color.Black,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
        // Dislike button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surface)
                .clickable(onClick = onDislike)
                .selectable(
                    selected = false,
                    onClick = onDislike,
                    indication = ripple(
                        radius = 65.dp,
                        color = Color(0xFFEF5350)
                    ),
                    interactionSource = interactionSourceDislike
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = stringResource(R.string.tweetscreen_text_dislike),
                tint = if (isDisliked) Color(0xFFEF5350) else Color.Gray,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(180f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$dislikesCount",
                color = if (isDisliked) Color(0xFFEF5350) else Color.Black,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun SimpleSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = {
            Text(
                text = stringResource(R.string.tweetscreen_placeholder_search),
                style = MaterialTheme.typography.bodySmall
            )
        },
        modifier = modifier
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.tweetscreen_icon_search_description),
                modifier = Modifier.clickable {
                    focusManager.clearFocus()
                }
            )
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.tweetscreen_icon_description_clear),
                    modifier = Modifier.clickable {
                        onQueryChange("")
                        focusManager.clearFocus()
                    }
                )
            }
        }
    )
}


