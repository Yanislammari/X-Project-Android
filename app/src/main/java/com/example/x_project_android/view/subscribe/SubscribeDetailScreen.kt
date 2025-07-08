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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.x_project_android.data.models.User
import com.example.x_project_android.event.GlobalEvent
import com.example.x_project_android.event.GlobalEventBus
import com.example.x_project_android.event.NavEventBus
import com.example.x_project_android.utils.reduceText
import com.example.x_project_android.view.compose.DisplayLoader
import com.example.x_project_android.view.compose.DisplayRoundImage
import com.example.x_project_android.view.tweet.TweetCell
import com.example.x_project_android.viewmodels.subscribe.SubscribeDetailViewModel
import com.example.x_project_android.viewmodels.subscribe.SubscriptionDetailScreenDest

@Composable
fun SubscribeDetailScreen(
    navHostController: NavHostController,
    subscribeDetailViewModel: SubscribeDetailViewModel,
) {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                DisplayProfileDetail(
                    user = subscribeDetailViewModel.userDetail.value,
                    updatedSubscribe = {
                        subscribeDetailViewModel.statusSubscribe()
                    }
                )
            }
            item {
                HorizontalDivider()
            }
            item {
                if (subscribeDetailViewModel.isLoading.value) {
                    DisplayLoader()
                } else if (subscribeDetailViewModel.tweetProfile.isEmpty()) {
                    Text(
                        text = "No tweets found",
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    subscribeDetailViewModel.tweetProfile.forEach { tweet ->
                        TweetCell(
                            tweet = tweet,
                            searchValue = "",
                            onClick = {
                                subscribeDetailViewModel.navigateToTweetDetail(tweet)
                                navHostController.navigate(
                                    SubscriptionDetailScreenDest.ROUTE + "/" + tweet.user.id
                                )
                            },
                            maxLength = 100,
                            imageHeight = 100,
                            onPseudoClick = {},
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
    updatedSubscribe: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val fixedHeight = screenHeight / 1.5f
    val modifierText =
        Modifier.padding(top = 16.dp, bottom = 16.dp, start = screenWidth / 2.5f, end = 8.dp)

    Box(
        modifier = Modifier
            .height(fixedHeight)   // Limit height to one third of screen height
            .padding(bottom = 16.dp)
            .fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .weight(0.25f)
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
                    .weight(1f)
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
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(start = 16.dp, bottom = screenHeight / 2.9f)
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
                            onClick = updatedSubscribe,
                        )
                )
            }
        }

    }
}

