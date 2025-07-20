package com.example.x_project_android.view.register

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.x_project_android.R
import com.example.x_project_android.view.compose.DisplayLoader
import com.example.x_project_android.view.navigationcompose.navigateDeleteAllRoute
import com.example.x_project_android.viewmodels.RegisterUIEvent
import com.example.x_project_android.viewmodels.RegisterViewModel

@Composable
fun PasswordScreen(
    registerViewModel: RegisterViewModel,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        registerViewModel.uiEvent.collect { event ->
            when (event) {
                is RegisterUIEvent.NavigateTo -> {
                    navHostController.navigateDeleteAllRoute("home")
                }

                is RegisterUIEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    registerViewModel.setLoading(false)
                }
            }
        }
    }
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.passwordscreen_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (registerViewModel.isLoading.value) {
                DisplayLoader()
            } else {
                PasswordForm(
                    password = registerViewModel.password.value,
                    confirmPassword = registerViewModel.confirmPassword.value,
                    onPasswordChange = registerViewModel::setPassword,
                    onConfirmPasswordChange = registerViewModel::setConfirmPassword,
                    onSubmit = { registerViewModel.tryRegister(context) }
                )
            }
        }
    }
}

@Composable
fun PasswordForm(
    password: String,
    confirmPassword: String,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.passwordscreen_placeholder_password)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text(stringResource(R.string.passwordscreen_placeholder_confirm_passwd)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
        )

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            Text(stringResource(R.string.choseemailscreen_button))
        }
    }
}