package com.stocardapp.hackschoolchat.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity
data class ChatMessage(

        @PrimaryKey(autoGenerate = true)
        var uid: Int? = null,

        @Json(name = "name")
        @ColumnInfo(name = "name")
        var name: String,

        @Json(name = "message")
        @ColumnInfo(name = "message")
        var message: String

)