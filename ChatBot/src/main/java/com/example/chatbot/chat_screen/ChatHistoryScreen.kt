package com.example.chatbot.chat_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.tools.gamebooster.model.util.function_utils.convertLongToDateTime
import com.tools.gamebooster.ui.dialogs.ClearAllChatsConfirmation
import com.tools.gamebooster.ui.shapes.HexagonalShape
import com.tools.gamebooster.views.chat_screen.components.ResponseMessage
import com.tools.gamebooster.views.chat_screen.components.UserMessage
import com.tools.gamebooster.views.chat_screen.components.drawVerticalScrollbar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHistoryScreen(navController: NavHostController, viewModel: ChatViewModel) {

    val chats = viewModel.oldChats.collectAsState()

    val lazyListState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var clearAllChatsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (chats.value.isNotEmpty()) {
                    lazyListState.animateScrollToItem(chats.value.lastIndex) // Scroll to the last item
                }
            }
        }
    }

    ClearAllChatsConfirmation(
        showDialog = clearAllChatsDialog,
        onDismiss = { clearAllChatsDialog = false }) {
        clearAllChatsDialog = false
        viewModel.clearAllChatHistory()
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        modifier = Modifier.size(45.dp),
                        contentDescription = "Back"
                    )
                }
            },
            title = {
                Text(
                    text = buildAnnotatedString {
                        append("Chat ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W500)) {
                            append("History")
                        }
                    },
                    style = TextStyle(fontSize = 26.sp),
                )
            },
            actions = {
                OutlinedButton(
                    onClick = {
                       clearAllChatsDialog = true
                    },
                    shape = HexagonalShape(6.dp)
                ) {
                    Text("Clear", color = MaterialTheme.colorScheme.error, fontSize = 18.sp)
                    Icon(
                        imageVector = Icons.Outlined.Delete, contentDescription = "delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .drawVerticalScrollbar(lazyListState)
                ) {
                    items(chats.value) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 6.dp, top = 18.dp, end = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = convertLongToDateTime(it.time),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            )
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "remove chat",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable {
                                        viewModel.deleteOneChat(it)
                                    })
                        }
                        UserMessage(message = it.userMessage)
                        ResponseMessage(responseMessage = it.response)
                    }
                }
            }
        }
    }
}