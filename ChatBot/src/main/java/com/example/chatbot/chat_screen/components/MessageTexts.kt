package com.tools.gamebooster.views.chat_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResponseMessage(modifier: Modifier = Modifier, responseMessage: String) {
    val annotatedString = buildAnnotatedString {
        appendStyledText(responseMessage)
    }

    Text(
//        text = responseMessage,
        text = annotatedString,
        fontSize = 20.sp,
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()
    )
}

private fun AnnotatedString.Builder.appendStyledText(text: String) {
    val sentences = text.split("**")
    sentences.forEachIndexed { index, sentence ->
        if (index % 2 == 0) {
            append(sentence.trim().removeSuffix("*"))
            appendLine()
        } else {
            // Text between ** **
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            ) {
                appendLine()
//                append(sentence.trim())
                append("$sentence ".removePrefix("*"))
            }
        }
    }
}


@Composable
fun UserMessage(modifier: Modifier = Modifier, message: String) {
    Text(
        text = message,
        fontSize = 25.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface.copy(.9f),
        lineHeight = 30.sp,
        modifier = modifier
            .padding(start = 4.dp, bottom = 4.dp, end = 4.dp)
            .fillMaxWidth()
    )
}