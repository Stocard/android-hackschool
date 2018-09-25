package com.stocard.coolchat.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


@Database(
        entities = [Message::class],
        version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}