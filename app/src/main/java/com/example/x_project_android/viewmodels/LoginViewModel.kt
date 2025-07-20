package com.example.x_project_android.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.x_project_android.repositories.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
): ViewModel() {
    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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
         if(email.isBlank() || password.isBlank()) {
            _uiEvent.trySend(LoginUiEvent.Error("Email and Password cannot be empty"))
            return
         }
        authRepository.postLogin(email, password) { result ->
            when (result) {
                is LoginResult.Success -> {
                    _uiEvent.trySend(LoginUiEvent.Success(result.message))
                }
                is LoginResult.Failure -> {
                    _uiEvent.trySend(LoginUiEvent.Error(result.message))
                }
                is LoginResult.Error -> {
                    _uiEvent.trySend(LoginUiEvent.Error(result.message))
                }
            }
        }
    }
}

sealed class LoginResult {
    data class Success(val message: String) : LoginResult()
    data class Failure(val message: String) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

sealed class LoginUiEvent {
    data class Success(val message: String) : LoginUiEvent()
    data class Error(val message: String) : LoginUiEvent()
}