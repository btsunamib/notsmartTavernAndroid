package com.sillytavern.android.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    val annotatedString = parseMarkdown(text)
    
    Text(
        text = annotatedString,
        modifier = modifier,
        color = color
    )
}

private fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0
        
        while (currentIndex < text.length) {
            when {
                text.startsWith("**", currentIndex) -> {
                    val endIndex = text.indexOf("**", currentIndex + 2)
                    if (endIndex != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(text.substring(currentIndex + 2, endIndex))
                        }
                        currentIndex = endIndex + 2
                    } else {
                        append(text[currentIndex])
                        currentIndex++
                    }
                }
                text.startsWith("*", currentIndex) && !text.startsWith("**", currentIndex) -> {
                    val endIndex = text.indexOf("*", currentIndex + 1)
                    if (endIndex != -1 && endIndex > currentIndex + 1) {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(text.substring(currentIndex + 1, endIndex))
                        }
                        currentIndex = endIndex + 1
                    } else {
                        append(text[currentIndex])
                        currentIndex++
                    }
                }
                text.startsWith("`", currentIndex) -> {
                    val endIndex = text.indexOf("`", currentIndex + 1)
                    if (endIndex != -1 && endIndex > currentIndex + 1) {
                        append(text.substring(currentIndex + 1, endIndex))
                        currentIndex = endIndex + 1
                    } else {
                        append(text[currentIndex])
                        currentIndex++
                    }
                }
                text.startsWith("\n", currentIndex) -> {
                    append("\n")
                    currentIndex++
                }
                else -> {
                    append(text[currentIndex])
                    currentIndex++
                }
            }
        }
    }
}
