package com.example.x_project_android.view.navigationcompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.view.compose.navbar.SubscribeScreenDest
import com.example.x_project_android.view.compose.navbar.TweetScreenDest
import com.example.x_project_android.view.subscribe.SubscribeDetailScreen
import com.example.x_project_android.view.subscribe.SubscribeScreen
import com.example.x_project_android.view.tweet.TweetDetailScreen
import com.example.x_project_android.view.tweet.TweetScreen
import com.example.x_project_android.viewmodels.subscribe.SharedSubscribeViewModel
import com.example.x_project_android.viewmodels.subscribe.SubscribeDetailViewModel
import com.example.x_project_android.viewmodels.subscribe.SubscribeViewModel
import com.example.x_project_android.viewmodels.subscribe.SubscriptionDetailScreenDest
import com.example.x_project_android.viewmodels.tweet.AddTweetViewModel
import com.example.x_project_android.viewmodels.tweet.SharedTweetViewModel
import com.example.x_project_android.viewmodels.tweet.TweetDetailScreenDest
import com.example.x_project_android.viewmodels.tweet.TweetDetailViewModel
import com.example.x_project_android.viewmodels.tweet.TweetsViewModel

@Composable
fun AppNavigation(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val addTweetViewModel : AddTweetViewModel = viewModel()

    val tweetsViewModel: TweetsViewModel = viewModel()
    val sharedTweetViewModel: SharedTweetViewModel = viewModel()

    val subscribeViewModel: SubscribeViewModel = viewModel()
    val sharedSubscribeViewModel: SharedSubscribeViewModel = viewModel()

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = TweetScreenDest.route,
    ) {
        composable(TweetScreenDest.route) {
            TweetScreen(
                navHostController,
                tweetsViewModel,
                sharedTweetViewModel,
                sharedSubscribeViewModel
            )
        }
        composable(SubscribeScreenDest.route) {
            SubscribeScreen(navHostController, subscribeViewModel, sharedSubscribeViewModel)
        }

        composable(
            route = TweetDetailScreenDest.FULLROUTE,
            arguments = listOf(
                navArgument(TweetDetailScreenDest.TWEETIDARG) { type = NavType.StringType },
                navArgument("origin") {
                    type = NavType.StringType
                    nullable = false
                    defaultValue = "default"
                }
            )
        ) { backStackEntry ->
            val tweetDetailViewModel: TweetDetailViewModel = viewModel()
            val tweetId = backStackEntry.arguments?.getString(TweetDetailScreenDest.TWEETIDARG)
            if (tweetId == null) {
                LaunchedEffect(Unit) {
                    navHostController.popBackStack()
                }
                return@composable
            }

            LaunchedEffect(tweetId) {
                tweetDetailViewModel.setTweetId(tweetId)
            }
            TweetDetailScreen(
                navHostController,
                tweetDetailViewModel,
                sharedTweetViewModel,
                sharedSubscribeViewModel,
                origin = backStackEntry.arguments?.getString("origin") ?: "default"
            )
        }

        composable(
            route = SubscriptionDetailScreenDest.FULLROUTE,
            arguments = listOf(navArgument(SubscriptionDetailScreenDest.USERIDARG) {
                type = NavType.StringType
            })
        ) {
            val subscribeDetailViewModel: SubscribeDetailViewModel = viewModel()
            val userId = it.arguments?.getString(SubscriptionDetailScreenDest.USERIDARG)
            if (userId == null) {
                LaunchedEffect(Unit) {
                    navHostController.popBackStack()
                }
                return@composable
            }
            LaunchedEffect(userId) {
                subscribeDetailViewModel.setUserId(userId)
            }
            SubscribeDetailScreen(
                navHostController,
                subscribeDetailViewModel,
                sharedTweetViewModel,
                sharedSubscribeViewModel
            )
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