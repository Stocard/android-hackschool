package com.stocardapp.hackschoolchat

import android.app.Application
import androidx.room.Room
import com.stocardapp.hackschoolchat.database.AppDatabase
import timber.log.Timber

class ChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // FIXME: inject?
        database = Room.databaseBuilder(this, AppDatabase::class.java, "chat_db")
                .fallbackToDestructiveMigration()
                .build()
    }

    companion object {
        lateinit var database: AppDatabase
    }

}