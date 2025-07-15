package com.example.x_project_android.view.tweet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.x_project_android.viewmodels.tweet.AddTweetViewModel

//Max = 1000
//No image height

@Composable
fun AddTweetScreen(
    navHostController: NavHostController,
    addTweetViewModel: AddTweetViewModel
){
    Scaffold {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .padding(16.dp)
        ){
            AddImage()
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            AddContent()
        }
    }
}

@Composable
fun AddImage(
    // Add parameters as needed
) {
    Text("AAAA")
}

@Composable
fun AddContent(

){
    Text("BBBB")
}