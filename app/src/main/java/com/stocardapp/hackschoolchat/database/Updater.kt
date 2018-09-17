package com.stocardapp.hackschoolchat.database

import android.content.Context
import com.stocardapp.hackschoolchat.ChatApplication
import com.stocardapp.hackschoolchat.backend.Backend
import retrofit2.Response
import timber.log.Timber

// TODO: make this a UpdateService - singleton?
class Updater(
        val context: Context
) {

    fun update() {
        Timber.d("Updating")
        val response: Response<List<ChatMessage>>? = Backend.instance.getMessages().execute()
        val messages: List<ChatMessage> = response?.body()!! // TODO add null handling

        messages.forEach {
            ChatApplication.database.chatDao().insert(it)
        }
        Timber.d("Inserted ${messages.size} messages")
    }

}