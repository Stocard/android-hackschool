package com.stocardapp.hackschoolchat.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(
        tableName = "chats",
        indices = [Index(
                value = arrayOf("timestamp"),
                unique = true
        )]
)
data class ChatMessage(

        @Json(name = "timestamp")
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "timestamp")
        var timestamp: Long = Long.MAX_VALUE,

        @Json(name = "name")
        @ColumnInfo(name = "name")
        var name: String,

        @Json(name = "message")
        @ColumnInfo(name = "message")
        var message: String


)