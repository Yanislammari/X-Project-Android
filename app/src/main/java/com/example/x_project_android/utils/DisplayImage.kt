package com.example.x_project_android.utils

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.x_project_android.R


@Composable
fun DisplayRoundImage(uri: Uri?) {
    AsyncImage(
        model = uri,
        contentDescription = stringResource(R.string.pickimagescreen_iùage_description),
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.Gray),
        contentScale = ContentScale.Crop
    )
}