package com.example.x_project_android.view.subscribe

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.x_project_android.R
import com.example.x_project_android.data.models.User
import com.example.x_project_android.event.SendGlobalEvent
import com.example.x_project_android.utils.reduceText
import com.example.x_project_android.view.compose.DisplayLoader
import com.example.x_project_android.view.compose.DisplayRoundImage
import com.example.x_project_android.view.tweet.TweetCell
import com.example.x_project_android.viewmodels.subscribe.SharedSubscribeViewModel
import com.example.x_project_android.viewmodels.subscribe.SubscribeDetailViewModel
import com.example.x_project_android.viewmodels.tweet.SharedTweetViewModel
import com.example.x_project_android.viewmodels.tweet.TweetDetailScreenDest
import kotlinx.coroutines.launch

@Composable
fun SubscribeDetailScreen(
    navHostController: NavHostController,
    subscribeDetailViewModel: SubscribeDetailViewModel,
    sharedTweetViewModel: SharedTweetViewModel,
    sharedSubscribeViewModel: SharedSubscribeViewModel
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        if (subscribeDetailViewModel.userDetail.value == null) {
            if (sharedSubscribeViewModel.user == null) {
                // fetch using id
            } else {
                subscribeDetailViewModel.setUserDetail(sharedSubscribeViewModel.user)
            }
        }
        subscribeDetailViewModel.fetchTweetFromUserId(subscribeDetailViewModel.userId.value)
    }

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ) {
            item {
                DisplayProfileDetail(
                    user = subscribeDetailViewModel.userDetail.value,
                    onClick = {
                        subscribeDetailViewModel.sendEventStateSub()
                    }
                )
            }
            item {
                if (subscribeDetailViewModel.isLoading.value) {
                    DisplayLoader()
                } else if (subscribeDetailViewModel.tweetProfile.isEmpty()) {
                    Text(
                        text = stringResource(R.string.subdetailscreen_error_no_tweet),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp), // padding above
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    subscribeDetailViewModel.tweetProfile.forEach { tweet ->
                        TweetCell(
                            tweet = tweet,
                            searchValue = "",
                            onClick = {
                                sharedTweetViewModel.setTweetShared(tweet)
                                navHostController.navigate(TweetDetailScreenDest.ROUTE + "/${tweet.id}?origin=subscribe")
                            },
                            maxLength = 100,
                            imageHeight = 100,
                            onPseudoClick = {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            },
                            onLike = { SendGlobalEvent.onLikeTweet(tweet.id) },
                            onDislike = { SendGlobalEvent.onDislikeTweet(tweet.id) },
                        )
                    }
                }
            }
        }
    }

}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DisplayProfileDetail(
    user: User?,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val modifierText =
        Modifier.padding(top = 16.dp, bottom = 16.dp, start = screenWidth / 2.5f, end = 8.dp)

    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column {
            Box(
                modifier = Modifier
                    .height(75.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = reduceText(user?.pseudo, 25),
                    color = Color.White,
                    modifier = modifierText,
                )
            }
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = reduceText(user?.bio, 250, "No bio"),
                    color = Color.Black,
                    modifier = modifierText,
                )
            }
            DisplayDynamicBio(
                screenWidth = screenWidth,
                isExpanded = isExpanded,
                onClick = {
                    isExpanded = !isExpanded
                }
            )
        }
        DisplayProfileImage(
            user = user,
            screenWidth = screenWidth,
            onClick = onClick
        )
    }
}

@Composable
fun DisplayProfileImage(
    user: User?,
    screenWidth: Dp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(start = 16.dp, top = 25.dp)
    ) {
        DisplayRoundImage(
            uri = user?.imageUri,
            size = 100
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            modifier = Modifier
                .width(screenWidth / 4)
                .padding(bottom = 16.dp),
        )
        Text(
            text = if (user?.isSubscribed == true) "Unsubscribe" else "Subscribe",
            color = if (user?.isSubscribed == true) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable(
                    onClick = onClick,
                )
        )
    }
}

@Composable
fun DisplayDynamicBio(
    screenWidth: Dp,
    isExpanded: Boolean,
    onClick: () -> Unit
){
    val dividerSize = screenWidth / 2.7f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick()
            }
    ) {
        HorizontalDivider(
            modifier = Modifier
                .width(dividerSize)
                .align(Alignment.CenterStart)
        )
        HorizontalDivider(
            modifier = Modifier
                .width(dividerSize)
                .align(Alignment.CenterEnd)
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                .align(Alignment.Center)
        ) {
            ExpandableIcon(
                isExpanded = isExpanded,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ExpandableIcon(
    isExpanded: Boolean,
    modifier: Modifier
) {
    val icon = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    Icon(
        imageVector = icon,
        contentDescription = if (isExpanded)
            stringResource(R.string.subscribedetailscreen_content_desc_biodisplay_colapse)
            else stringResource(R.string.expand),
        tint = Color.White,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DisplayProfilePreview() {
    val user = User(
        pseudo = "John Doe aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        bio = "This is a sample bio that might be quite long, but we’ll limit it for display purposes.",
        imageUri = null,
        isSubscribed = false
    )

    DisplayProfileDetail(user = user, onClick = {})
}

