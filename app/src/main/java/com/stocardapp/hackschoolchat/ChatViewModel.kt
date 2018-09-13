package com.stocardapp.hackschoolchat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Room
import com.airbnb.epoxy.EpoxyModel
import com.stocardapp.hackschoolchat.chats.ChatViewModel_
import com.stocardapp.hackschoolchat.database.AppDatabase
import com.stocardapp.hackschoolchat.database.ChatMessage
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.coroutineScope

class ChatViewModel(context: Application) : AndroidViewModel(context) {

    private val database: AppDatabase by lazy { Room.databaseBuilder(context, AppDatabase::class.java, "chat_db").build() }

    fun chats(): LiveData<List<EpoxyModel<*>>> {
        return Transformations.map(database.chatDao().allLive) { input: MutableList<ChatMessage> ->
            input.map {
                val model = ChatViewModel_()
                model.id(it.uid)
                model.value(it)
            }
        }
    }

    suspend fun send(message: String) = coroutineScope {
        database.chatDao().insertAll(ChatMessage(name = "Tobias", message = message))
    }

}