package com.example.x_project_android.view.compose.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.x_project_android.view.subscribe.SubscribeScreen

interface BasicAppDestination {
    val icon: ImageVector
    val name: String
    val route: String
}

object TweetScreenDest: BasicAppDestination {
    override val icon = Icons.Default.Home
    override val name: String = "Accueil"
    override val route: String = "screens/1"
}

object SubscribeScreenDest: BasicAppDestination {
    override val icon = Icons.Default.Person
    override val name: String = "Subscribe"
    override val route: String = "screens/2"
}


val navTabs = listOf(TweetScreenDest,SubscribeScreenDest)