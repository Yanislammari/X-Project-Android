package com.example.x_project_android.utils

fun getRelativeTime(timestamp: Long?): String {
    if (timestamp == null) return ""

    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val second = 1000L
    val minute = 60 * second
    val hour = 60 * minute
    val day = 24 * hour

    return when {
        diff < minute -> "just now"
        diff < hour -> {
            val mins = diff / minute
            "$mins min${if (mins > 1) "s" else ""}"
        }
        diff < day -> {
            val hours = diff / hour
            "$hours hour${if (hours > 1) "s" else ""}"
        }
        else -> {
            val days = diff / day
            "$days day${if (days > 1) "s" else ""}"
        }
    }
}