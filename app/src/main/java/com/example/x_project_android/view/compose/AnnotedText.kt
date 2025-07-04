package com.example.x_project_android.view.compose

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.x_project_android.utils.reduceText

fun buildHighlightedText(text: String?, query: String, highlightColor : Color): AnnotatedString {
    val content = reduceText(text,350,"No content available")
    if (query.isBlank()) return AnnotatedString(content)

    val lowerContent = content.lowercase()
    val lowerQuery = query.lowercase()

    val annotatedString = buildAnnotatedString {
        var startIndex = 0
        while (true) {
            val index = lowerContent.indexOf(lowerQuery, startIndex)
            if (index == -1) {
                append(content.substring(startIndex))
                break
            }

            append(content.substring(startIndex, index))
            withStyle(
                SpanStyle(
                    background = highlightColor.copy(alpha = 0.3f),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            ) {
                append(content.substring(index, index + query.length))
            }
            startIndex = index + query.length
        }
    }
    return annotatedString
}