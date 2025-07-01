package com.example.x_project_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.x_project_android.navigationcompose.AppNavigation
import com.example.x_project_android.ui.theme.AppTheme
import com.example.x_project_android.viewmodels.LoginViewModel
import com.example.x_project_android.view.LoginView
import com.example.x_project_android.viewmodels.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val loginViewModel: LoginViewModel = viewModel()
                val registerViewModel: RegisterViewModel = viewModel()
                AppNavigation(
                    navHostController = navController,
                    loginViewModel = loginViewModel,
                    registerViewModel = registerViewModel
                )
            }
        }
    }
}
