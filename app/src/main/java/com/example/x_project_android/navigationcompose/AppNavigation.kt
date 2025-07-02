package com.example.x_project_android.navigationcompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.x_project_android.view.LoginView
import com.example.x_project_android.view.register.BioDescScreen
import com.example.x_project_android.view.register.PickImageScreen
import com.example.x_project_android.view.register.ChoseEmailScreen
import com.example.x_project_android.viewmodels.LoginViewModel
import com.example.x_project_android.viewmodels.RegisterViewModel

@Composable
fun AppNavigation(
    navHostController : NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
){
    NavHost(navController = navHostController, startDestination = "login"){
        composable("login"){
            LoginView(loginViewModel,navHostController)
        }
        composable("register") {
            BioDescScreen(registerViewModel,navHostController)
        }
        composable("register/image_screen"){
            PickImageScreen(registerViewModel, navHostController)
        }
        composable("register/email"){
            ChoseEmailScreen(registerViewModel,navHostController)
        }
    }
}