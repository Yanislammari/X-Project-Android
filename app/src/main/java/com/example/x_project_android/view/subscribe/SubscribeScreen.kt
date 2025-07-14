package com.example.x_project_android.view.subscribe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.x_project_android.R
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import com.example.x_project_android.utils.reduceText
import com.example.x_project_android.view.compose.DisplayLoader
import com.example.x_project_android.view.compose.DividerHorizontal
import com.example.x_project_android.view.tweet.DisplayPseudo
import com.example.x_project_android.viewmodels.subscribe.SharedSubscribeViewModel
import com.example.x_project_android.viewmodels.subscribe.SubscribeViewModel
import com.example.x_project_android.viewmodels.subscribe.SubscriptionDetailScreenDest
import com.example.x_project_android.viewmodels.tweet.imageTest

@Composable
fun SubscribeScreen(
    navHostController: NavHostController,
    subscribeViewModel: SubscribeViewModel,
    sharedSubscribeViewModel: SharedSubscribeViewModel
) {
    LaunchedEffect(Unit) {
        subscribeViewModel.fetchSubscriptions()
    }
    Scaffold(
        topBar = {
            Text(
                text = "Subscriptions",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            item {
                if (subscribeViewModel.isLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(), // fills available space inside LazyColumn
                        contentAlignment = Alignment.Center
                    ) {
                        DisplayLoader()
                    }
                }
            }

            item {
                if (!subscribeViewModel.isLoading.value && subscribeViewModel.subscriptionsProfile.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.subscribescreen_no_tweet_error),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            items(subscribeViewModel.subscriptionsProfile) { tweet ->
                SubscribeCell(
                    tweet = tweet,
                    onClick = {
                        sharedSubscribeViewModel.setUserShared(tweet.user)
                        navHostController.navigate(SubscriptionDetailScreenDest.ROUTE + "/${tweet.user.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun SubscribeCell(
    tweet: Tweet,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = MaterialTheme.colorScheme.secondary)
            )
            .padding(horizontal = 8.dp, vertical = 16.dp),
    ) {
        DisplayPseudo(
            user = tweet.user,
            tweet = tweet,
            formatedPseudo = "Posted by ${reduceText(tweet.user.pseudo, 25)}",
            onClick = onClick
        )
        Text(
            text = reduceText(tweet.content, 50, "..."),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.Start
        )

        DividerHorizontal()
    }
}

@Preview
@Composable
fun SubscribeCellPreview() {
    SubscribeCell(
        tweet = Tweet(
            id = "1",
            content = "This is a sample tweet content that is quite long and should be truncated for the preview.",
            user = User(
                id = "user1",
                pseudo = "SampleUser",
                imageUri = imageTest
            ),
            timestamp = System.currentTimeMillis()
        ),
        onClick = {}
    )

}