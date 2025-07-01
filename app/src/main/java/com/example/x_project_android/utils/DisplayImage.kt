package com.example.x_project_android.utils

import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.x_project_android.R


@Composable
fun DisplayRoundImage(uri: Uri?) {
    AsyncImage(
        model = uri,
        contentDescription = stringResource(R.string.pickimagescreen_iùage_description),
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.Gray)
    )
}