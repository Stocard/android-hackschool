package com.stocardapp.hackschoolchat.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(tableName = "chats")
data class ChatMessage(

        @PrimaryKey(autoGenerate = false)
        @Json(name = "timestamp")
        @ColumnInfo(name = "timestamp")
        var timestamp: Long? = null,

        @Json(name = "name")
        @ColumnInfo(name = "name")
        var name: String,

        @Json(name = "message")
        @ColumnInfo(name = "message")
        var message: String


        )