package com.example.x_project_android.navigationcompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.x_project_android.view.LoginView
import com.example.x_project_android.view.compose.navbar.NavScreen
import com.example.x_project_android.view.register.BioDescScreen
import com.example.x_project_android.view.register.PickImageScreen
import com.example.x_project_android.view.register.ChoseEmailScreen
import com.example.x_project_android.view.register.PasswordScreen
import com.example.x_project_android.viewmodels.LoginViewModel
import com.example.x_project_android.viewmodels.RegisterViewModel

@Composable
fun AuthNavigation(
    navHostController: NavHostController,
){
    NavHost(navController = navHostController, startDestination = "login"){
        composable("login"){
            val loginViewModel: LoginViewModel = viewModel()
            LoginView(loginViewModel,navHostController)
        }
        composable("register") {
            val registerViewModel: RegisterViewModel = viewModel()
            BioDescScreen(registerViewModel,navHostController)
        }
        composable("register/image_screen"){
            val registerViewModel: RegisterViewModel = viewModel()
            PickImageScreen(registerViewModel, navHostController)
        }
        composable("register/email"){
            val registerViewModel: RegisterViewModel = viewModel()
            ChoseEmailScreen(registerViewModel,navHostController)
        }
        composable("register/password") {
            val registerViewModel: RegisterViewModel = viewModel()
            PasswordScreen(registerViewModel,navHostController)
        }

        composable("home") {
            NavScreen(navHostController)
        }
    }
}

fun NavHostController.navigateDeleteAllRoute(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateDeleteAllRoute.graph.findStartDestination().id
        ) {
            inclusive = true
            saveState = false
        }
        launchSingleTop = true
        restoreState = false
    }
