package com.tools.gamebooster.model.room_database.chats

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatsDao {
    @Query("SELECT * FROM chat")
    fun getAllChats(): List<Chat>

    @Query("SELECT * FROM chat")
    fun getAllChatsFlow(): Flow<List<Chat>>

    @Insert
    suspend fun addChat(chat: Chat)

    @Delete
    suspend fun deleteChat(chat: Chat)

    @Query("DELETE FROM chat")
    suspend fun deleteAllChats()
}