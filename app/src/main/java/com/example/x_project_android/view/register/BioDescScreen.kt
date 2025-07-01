package com.example.x_project_android.view.register

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.x_project_android.R
import com.example.x_project_android.ui.theme.AppTheme
import com.example.x_project_android.viewmodels.RegisterUIEvent
import com.example.x_project_android.viewmodels.RegisterViewModel

@Composable
fun BioDescScreen(
    registerViewModel : RegisterViewModel,
    navHostController : NavHostController
){
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        registerViewModel.uiEvent.collect { event ->
            when (event) {
                is RegisterUIEvent.NavigateTo -> {
                    navHostController.navigate("register/image_screen")
                }
                is RegisterUIEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Scaffold{ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.bio_desc_screen_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = registerViewModel.pseudo.value,
                onValueChange = { registerViewModel.setPseudo(it) },
                label = { Text(stringResource(R.string.bio_desc_screen_placeholder_pseudo)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = registerViewModel.bio.value,
                onValueChange = {
                    registerViewModel.setBio(it);
                },
                label = { Text(stringResource(R.string.bio_desc_screen_placeholder_bio)) },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                minLines = 5,
                maxLines = 15,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Button(
                onClick = {
                    registerViewModel.goToImage()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text(stringResource(R.string.bio_desc_screen_button))
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun BioDescPreview() {
    AppTheme {
        BioDescScreen(
            registerViewModel = RegisterViewModel(),
            navHostController = NavHostController(LocalContext.current)
        )
    }
}