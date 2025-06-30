package com.example.x_project_android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private var email by mutableStateOf("")

    private var password by mutableStateOf("")

    fun onGetEmail(): String {
        return email
    }

    fun onGetPassword(): String {
        return password
    }

    // Custom setter functions to allow controlled modification
    fun onSetEmail(newEmail: String) {
        email = newEmail
    }

    fun onSetPassword(newPassword: String) {
        password = newPassword
    }

    fun sendLoginRequest() {
        // Use email and password
    }

    fun goToRegister() {
        // Logic to navigate to the registration screen
    }
}