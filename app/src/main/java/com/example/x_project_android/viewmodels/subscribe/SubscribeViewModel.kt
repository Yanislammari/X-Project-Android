package com.example.x_project_android.viewmodels.subscribe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.x_project_android.data.models.Tweet
import com.example.x_project_android.data.models.User
import com.example.x_project_android.viewmodels.tweet.imageTest

class SubscribeViewModel: ViewModel() {
    private var _subscriptionsProfile = mutableStateListOf<Tweet>()
    val subscriptionsProfile: List<Tweet> = _subscriptionsProfile

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    suspend fun fetchSubscriptions() {
        if (_subscriptionsProfile.isNotEmpty()) return
        _isLoading.value = true
        kotlinx.coroutines.delay(500)
        _subscriptionsProfile.clear()
        _subscriptionsProfile.addAll(
            listOf(
                Tweet(
                    id = "1",
                    content = "Voici mon chat Miaou !",
                    imageUri = imageTest,
                    user = User(
                        id = "1",
                        pseudo = "Alice",
                        imageUri = imageTest,
                    ),
                    timestamp = System.currentTimeMillis() - 160 * 1000L,
                    likesCount = 10,
                    dislikesCount = 10,
                ),
                Tweet(
                    id = "2",
                    content = "Un tres long texte chiaUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaajUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjUn tres long texte chiant a afffafhdjsjfjdsjnt a afffafhdjsjfjdsjfdkjsjdsjkfkjdskjfdskjkjfdskjfdskjkjfdskjfkjfdjkfskjfkjdsjjfjfjfjfjfjjfjfjfjfjlfdfdjskfslkfd joiezfhouzehfoiezhfoi hezofhzeo fezof hezoih",

                    user = User(
                        id = "1",
                        pseudo = "Exactemenf 25 charachter je le jure enfin je crois",
                        imageUri = imageTest,
                    ),
                    timestamp = System.currentTimeMillis() - 360 * 60 * 1000L,
                    likesCount = 10,
                    dislikesCount = 10,
                    isLiked = true,
                ),
                Tweet(
                    id = "3",
                    content = "Un tres long texte chiant a afffafhdjsjfjdsjfdkjsjdsjkfkjdskjfdskjkjfdskjfdskjkjfdskjfkjfdjkfskjfkjdsjjfjfjfjfjfjjfjfjfjfjlfdfdjskfslkfd joiezfhouzehfoiezhfoi hezofhzeo fezof hezoih",
                    imageUri = imageTest,
                    user = User(
                        id = "1",
                        pseudo = "Exactemenf 25 charachter je le jure enfin je crois",
                        imageUri = imageTest,
                    ),
                    timestamp = System.currentTimeMillis() - 240 * 60 * 60 * 1000L,
                    likesCount = 10,
                    dislikesCount = 10,
                    isDisliked = true,
                )
            )
        )

        _isLoading.value = false
    }
}