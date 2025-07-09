package com.example.x_project_android.viewmodels.subscribe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.x_project_android.data.models.User

class SharedSubscribeViewModel:ViewModel() {
    var user by mutableStateOf<User?>(null)
        private set

    fun setUserShared(newUser: User?) {
        user = newUser
    }
}