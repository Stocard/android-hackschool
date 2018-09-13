package com.stocardapp.hackschoolchat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Room
import com.airbnb.epoxy.EpoxyModel
import com.stocardapp.hackschoolchat.backend.Backend
import com.stocardapp.hackschoolchat.chats.ChatViewModel_
import com.stocardapp.hackschoolchat.database.AppDatabase
import com.stocardapp.hackschoolchat.database.ChatMessage
import kotlinx.coroutines.experimental.coroutineScope
import retrofit2.Response

class ChatViewModel(context: Application) : AndroidViewModel(context) {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "chat_db")
                .fallbackToDestructiveMigration()
                .build()
    }

    // TODO: set dynamically
    private var name: String? = null

    fun chats(): LiveData<List<EpoxyModel<*>>> {
        return Transformations.map(database.chatDao().getAllLive()) { input: List<ChatMessage> ->
            input.map {
                val model = ChatViewModel_()
                model.id(it.message)
                model.value(it)
            }
        }
    }

    suspend fun send(message: String): Response<ChatMessage> = coroutineScope {
        // TODO: name handing is temporary. Change.
        val n = name
        val chatMessage = if (n == null) {
            name = message
            ChatMessage(name = message, message = "Hi, I'am $name")
        } else {
            ChatMessage(name = n, message = message)
        }
        database.chatDao().insert(chatMessage)
        Backend.instance.post(chatMessage).execute()
    }

    fun update(messages: List<ChatMessage>) {
        // TODO: only update new entries instead
        database.chatDao().nukeTable()
        database.chatDao().insertAll(messages)
    }

}