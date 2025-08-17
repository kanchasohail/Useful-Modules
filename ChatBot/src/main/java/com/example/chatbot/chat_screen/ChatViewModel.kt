package com.example.chatbot.chat_screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.tools.gamebooster.BuildConfig
import com.tools.gamebooster.MainActivity
import com.tools.gamebooster.model.room_database.chats.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class ChatViewModel : ViewModel() {
    private val chatsDao = MainActivity.db.chatsDao()

    private val model = GenerativeModel(
//        modelName = "gemini-pro",
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val _oldChats = MutableStateFlow(emptyList<Chat>())
    val oldChats = _oldChats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<Chat>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()

    private val _currentQuery = MutableStateFlow<String?>(null)
    val currentQuery = _currentQuery.asStateFlow()

    fun sendChatMessage(userMessage: String, context: Context) {
        if (userMessage.isEmpty()) return

        viewModelScope.launch {
            _currentQuery.value = userMessage
            _isLoading.value = true

            try {
                val response = withContext(Dispatchers.IO) {
                    ensureActive()
                    model.generateContent(userMessage).text
                }
//                val response = model.generateContent(userMessage).text
                val thisChat = Chat(
                    id = null,
                    userMessage = userMessage,
                    response = response.toString(),
                    time = System.currentTimeMillis()
                )

                _currentQuery.value = null
                _isLoading.value = false
                _chatMessages.value += thisChat
                addChatToDb(thisChat)

            } catch (e: CancellationException) {
                Log.d("ChatViewModel", "Response generation was cancelled ${e.message.toString()}")
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error generating response", e)
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            } finally {
                withContext(NonCancellable) {
                    _isLoading.value = false
                    _currentQuery.value = null
                }
            }
        }
    }

    fun deleteOneChat(chat: Chat) {
        viewModelScope.launch {
            chatsDao.deleteChat(chat)
        }
    }

    fun clearAllChatHistory() {
        viewModelScope.launch {
            chatsDao.deleteAllChats()
        }
    }

    private fun addChatToDb(chat: Chat) {
        viewModelScope.launch {
            chatsDao.addChat(chat)
        }
    }


    init {
        viewModelScope.launch {
            chatsDao.getAllChatsFlow().collect { chats ->
                _oldChats.value = chats
            }
        }
    }

}