package com.example.chatbot.chat_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tools.gamebooster.MainViewModel
import com.tools.gamebooster.R
import com.tools.gamebooster.model.navigation.ScreenChatHistory
import com.tools.gamebooster.model.util.function_utils.rememberImeState
import com.tools.gamebooster.ui.dialogs.PromptLimitExceedAlert
import com.tools.gamebooster.ui.shapes.HexagonalShape
import com.tools.gamebooster.views.chat_screen.components.ResponseMessage
import com.tools.gamebooster.views.chat_screen.components.SendButton
import com.tools.gamebooster.views.chat_screen.components.UserMessage
import com.tools.gamebooster.views.chat_screen.components.drawVerticalScrollbar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavHostController,
    viewModel: ChatViewModel,
    mainViewModel: MainViewModel
) {

    val context = LocalContext.current

    val isLoading by viewModel.isLoading.collectAsState()
    val messages by viewModel.chatMessages.collectAsState()
    val currentQuery by viewModel.currentQuery.collectAsState()

    var userInput by remember { mutableStateOf("") }

    var showDialogState by remember { mutableStateOf(false) }

    var isFirstMessage by remember { mutableStateOf(true) }

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    fun scrollToBottom() {
        coroutineScope.launch {
            if (messages.isNotEmpty()) {
                lazyListState.animateScrollToItem(messages.lastIndex)
            }
        }
    }

    PromptLimitExceedAlert(
        showDialog = showDialogState,
        onDismiss = { showDialogState = false },
        onConfirm = { showDialogState = false })

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
                            append("Ai ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.W500)) {
                                append("Chatbot")
                            }
                        },
                        style = TextStyle(fontSize = 26.sp),
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(ScreenChatHistory) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.history_icon),
                            modifier = Modifier.size(30.dp),
                            contentDescription = "Chat History"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
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
                        .drawVerticalScrollbar(lazyListState),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = lazyListState
                ) {
                    item {
                        if (messages.isEmpty()) {
                            ResponseMessage(responseMessage = "Hi, How can I help you Today?")
                        }
                    }
                    items(messages) { message ->
                        Spacer(modifier = Modifier.height(8.dp))
                        UserMessage(message = message.userMessage)
                        ResponseMessage(responseMessage = message.response)
                    }
                    item {
                        if (currentQuery != null) {
                            UserMessage(message = currentQuery!!)
                        }
                    }
                }



                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        placeholder = { Text(text = "Ask Anything...") },
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (mainViewModel.canSendMessage() || isFirstMessage) {
                                    viewModel.sendChatMessage(userInput, context)
                                    userInput = ""
                                    scrollToBottom()
                                    isFirstMessage = false
                                } else {
                                    showDialogState = true
                                }
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        shape = HexagonalShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    SendButton(isLoading = isLoading, userInput = userInput) {
                        if (mainViewModel.canSendMessage() || isFirstMessage) {
                            viewModel.sendChatMessage(userInput, context)
                            userInput = ""
                            scrollToBottom()
                            isFirstMessage = false
                        } else {
                            showDialogState = true
                        }
                    }
                }

            }
        }
    }
}