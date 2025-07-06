package com.example.x_project_android.viewmodels.subscribe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

object SubscriptionDetailScreenDest {
    private const val ROUTE = "subscription_detail"
    const val USERIDARG = "userId"
    private const val USERIDPLACEHOLDER = "{$USERIDARG}"
    const val FULLROUTE = "$ROUTE/$USERIDPLACEHOLDER"
}

class SubscribeDetailViewModel: ViewModel() {
    private val _userId = mutableStateOf("")
    val userId: State<String> = _userId

    fun setUserId(id: String) {
        _userId.value = id
    }
}