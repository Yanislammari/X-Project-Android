package com.example.x_project_android.navigationcompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.x_project_android.view.compose.navbar.SubscribeScreenDest
import com.example.x_project_android.view.compose.navbar.TweetScreenDest
import com.example.x_project_android.view.subscribe.SubscribeScreen
import com.example.x_project_android.view.tweet.TweetDetailScreen
import com.example.x_project_android.view.tweet.TweetScreen
import com.example.x_project_android.viewmodels.subscribe.SubscribeViewModel
import com.example.x_project_android.viewmodels.tweet.SharedTweetViewModel
import com.example.x_project_android.viewmodels.tweet.TweetDetailScreenDest
import com.example.x_project_android.viewmodels.tweet.TweetDetailViewModel
import com.example.x_project_android.viewmodels.tweet.TweetsViewModel

@Composable
fun AppNavigation(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
){
    val sharedTweetViewModel: SharedTweetViewModel = viewModel()
    val tweetsViewModel: TweetsViewModel = viewModel()
    val subscribeViewModel: SubscribeViewModel = viewModel()
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = TweetScreenDest.route,
    ) {
        composable(TweetScreenDest.route) {
            TweetScreen(navHostController,tweetsViewModel, sharedTweetViewModel)
        }
        composable(SubscribeScreenDest.route) {
            SubscribeScreen(navHostController, subscribeViewModel)
        }

        composable(
            route = TweetDetailScreenDest.FULLROUTE,
            arguments = listOf(navArgument(TweetDetailScreenDest.TWEETIDARG) { type = NavType.StringType })
        ) {
            val tweetDetailViewModel: TweetDetailViewModel = viewModel()
            val tweetId = it.arguments?.getString(TweetDetailScreenDest.TWEETIDARG)
                ?: throw IllegalArgumentException("Tweet ID is required")

            LaunchedEffect(tweetId) {
                tweetDetailViewModel.setTweetId(tweetId)
            }
            TweetDetailScreen(navHostController, tweetsViewModel,tweetDetailViewModel, sharedTweetViewModel)
        }
    }
}

fun NavHostController.navigateSingleToTop(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleToTop.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }