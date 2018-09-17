package com.stocardapp.hackschoolchat

import android.app.Application
import timber.log.Timber

class ChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}