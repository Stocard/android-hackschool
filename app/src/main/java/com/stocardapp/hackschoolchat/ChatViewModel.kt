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

    private val database: AppDatabase by lazy { Room.databaseBuilder(context, AppDatabase::class.java, "chat_db").build() }

    // TODO: set dynamically
    private val name: String = "Paula"

    fun chats(): LiveData<List<EpoxyModel<*>>> {
        return Transformations.map(database.chatDao().allLive) { input: MutableList<ChatMessage> ->
            input.map {
                val model = ChatViewModel_()
                model.id(it.message)
                model.value(it)
            }
        }
    }

    suspend fun send(message: String): Response<ChatMessage> = coroutineScope {
        val chatMessage = ChatMessage(name = name, message = message)
        database.chatDao().insert(chatMessage)
        Backend.instance.post(chatMessage).execute()
    }

    fun update(messages: List<ChatMessage>) {
        database.chatDao().nukeTable()
        database.chatDao().insertAll(messages)
    }

}