package com.example.x_project_android.utils

fun reduceText(text: String?, maxLength: Int = 18,returnError : String = "Anonymous"): String {
    if (text.isNullOrEmpty()) return returnError
    return if (text.length > maxLength) {
        text.take(maxLength) + "..."
    } else {
        text
    }
}