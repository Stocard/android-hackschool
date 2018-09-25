package com.stocard.coolchat.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(

        @PrimaryKey
        @ColumnInfo(name = "timestamp_millis")
        val timestamp: Long,

        @ColumnInfo(name = "message")
        val message: String,

        @ColumnInfo(name = "from")
        val name: String

)