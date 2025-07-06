package com.example.x_project_android.viewmodels.subscribe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.x_project_android.data.models.User

class SharedSubscribeViewModel:ViewModel() {
    private val  _userDetail = mutableStateOf<User?>(null)
    val userDetail: State<User?> = _userDetail

    fun setUser(user: User?) {
        _userDetail.value = user
    }

    fun markAsUnsubscribed(subscribeViewModel: SubscribeViewModel) {
        _userDetail.value?.let { currentUser ->
            val updatedSubscription = currentUser.copy(isSubscribed = !currentUser.isSubscribed)
            _userDetail.value = updatedSubscription
        }
        if(_userDetail.value?.isSubscribed == false){
            subscribeViewModel.deleteWhenUnsubscribe(_userDetail.value?.id)
        }
        else{
            subscribeViewModel.addWhenSubscribe(_userDetail.value)
        }
    }
}