package com.stocardapp.hackschoolchat.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ChatMessage(

        @PrimaryKey(autoGenerate = true)
        var uid: Int? = null,

        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "message")
        var message: String

)