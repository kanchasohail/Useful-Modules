package com.tools.gamebooster.model.room_database.chats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Chat(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "user_message") val userMessage: String,
    @ColumnInfo(name = "response") val response: String,
    @ColumnInfo(name = "time") val time: Long
)