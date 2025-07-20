package com.example.x_project_android.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        bytes
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun byteArrayToRequestBody(bytes: ByteArray, mimeType: String = "image/jpeg"): RequestBody {
    return bytes.toRequestBody(mimeType.toMediaTypeOrNull())
}

