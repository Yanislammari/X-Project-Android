package com.example.x_project_android.view.register

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.x_project_android.R
import com.example.x_project_android.ui.theme.AppTheme
import com.example.x_project_android.view.compose.DisplayRoundImage
import com.example.x_project_android.utils.createImageUri
import com.example.x_project_android.viewmodels.RegisterUIEvent
import com.example.x_project_android.viewmodels.RegisterViewModel


@Composable
fun PickImageScreen(
    registerViewModel: RegisterViewModel,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val photoUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri.value?.let { registerViewModel.setImageUri(it) }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        registerViewModel.setImageUri(uri)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createImageUri(context)
            photoUri.value = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context,
                context.getString(R.string.pickimagescreen_permission_denied_error), Toast.LENGTH_SHORT).show()
        }
    }


    LaunchedEffect(Unit) {
        registerViewModel.uiEvent.collect { event ->
            when (event) {
                is RegisterUIEvent.NavigateTo -> {
                    navHostController.navigate("register/email")
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Title
            Text(
                text = stringResource(R.string.pickimagescreen_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (registerViewModel.imageUri.value != null) {
                DisplayRoundImage(
                    uri = registerViewModel.imageUri.value
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .padding(bottom = 24.dp)
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(text = stringResource(R.string.pickimagescreen_gallery_button))
                }
                Button(
                    onClick = {
                        val permission = android.Manifest.permission.CAMERA
                        if (context.checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                            val uri = createImageUri(context)
                            photoUri.value = uri
                            cameraLauncher.launch(uri)
                        } else {
                            cameraPermissionLauncher.launch(permission)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(text = stringResource(R.string.pickimagescreen_photo_button))
                }
            }
            Button(
                onClick = {registerViewModel.goToEmail()},
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(top = 16.dp),
            ) {
                Text(text = stringResource(R.string.pickimagescreen_button))
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun PickImagePreview() {
    AppTheme {
        PickImageScreen(
            registerViewModel = RegisterViewModel(),
            navHostController = NavHostController(LocalContext.current)
        )
    }
}