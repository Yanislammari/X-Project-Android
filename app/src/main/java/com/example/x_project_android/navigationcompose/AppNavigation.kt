package com.example.x_project_android.navigationcompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.x_project_android.view.compose.navbar.SubscribeScreenDest
import com.example.x_project_android.view.compose.navbar.TweetScreenDest
import com.example.x_project_android.view.subscribe.SubscribeScreen
import com.example.x_project_android.view.tweet.TweetScreen
import com.example.x_project_android.viewmodels.tweet.TweetsViewModel

@Composable
fun AppNavigation(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
){
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = TweetScreenDest.route,
    ) {
        composable(TweetScreenDest.route) {
            val tweetsViewModel:TweetsViewModel = viewModel()
            TweetScreen(navHostController,tweetsViewModel)
        }
        composable(SubscribeScreenDest.route) { SubscribeScreen(navHostController) }
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