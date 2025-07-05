package com.example.x_project_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.x_project_android.navigationcompose.AuthNavigation
import com.example.x_project_android.ui.theme.AppTheme
import com.example.x_project_android.viewmodels.tweet.SharedTweetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AuthNavigation(rememberNavController())
            }
        }
    }
}
