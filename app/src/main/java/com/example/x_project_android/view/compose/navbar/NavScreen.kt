package com.example.x_project_android.view.compose.navbar

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.x_project_android.navigationcompose.AppNavigation
import com.example.x_project_android.navigationcompose.navigateSingleToTop

@Composable
fun NavScreen(rootNavController: NavHostController) {
    val tabNavController = rememberNavController()

    val currentBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in navTabs.map { it.route }) {
                BasicNavBar(
                    modifier = Modifier.navigationBarsPadding(),
                    destinations = navTabs,
                    onTabSelected = { tab ->
                        tabNavController.navigateSingleToTop(tab.route)
                    },
                    currentDestination = navTabs.find { it.route == currentRoute } ?: TweetScreenDest
                )
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navHostController = tabNavController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


