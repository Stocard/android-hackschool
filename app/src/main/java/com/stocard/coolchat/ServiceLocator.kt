package com.stocard.coolchat

import android.content.Context
import com.stocard.coolchat.data.ChatRepository


class ServiceLocator private constructor(context: Context) {

    val chatRepository: ChatRepository by lazy { ChatRepository(context) }

    companion object {
        private var instance: ServiceLocator? = null

        @Synchronized
        fun get(context: Context): ServiceLocator {
            val instance = this.instance ?: ServiceLocator(context)
            this.instance = instance
            return instance
        }
    }
}