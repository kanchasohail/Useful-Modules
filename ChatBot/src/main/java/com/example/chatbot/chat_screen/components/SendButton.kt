package com.tools.gamebooster.views.chat_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tools.gamebooster.R
import com.tools.gamebooster.ui.layout.AnimatedCirclesFading

@Composable
fun SendButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    userInput: String,
    onSend: () -> Unit
) {

    Box(modifier.size(50.dp), contentAlignment = Alignment.Center) {
        if (isLoading) {
            AnimatedCirclesFading(MaterialTheme.colorScheme.primary)
        } else {
            IconButton(
                onClick = {
                    if (userInput.isNotBlank()) {
                        onSend()
                    }
                },
                modifier = Modifier
                    .background(
                        if (userInput.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            .4f
                        ),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_icon),
                    tint = if (userInput.isNotBlank()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(
                        .8f
                    ),
                    modifier = Modifier
                        .size(50.dp)
                        .padding(start = 4.dp, end = 2.dp),
                    contentDescription = "Send"
                )
            }
        }
    }
}