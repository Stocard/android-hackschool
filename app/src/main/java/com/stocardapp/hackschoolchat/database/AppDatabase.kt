package com.stocardapp.hackschoolchat.database


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChatMessage::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}
