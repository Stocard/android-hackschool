package com.stocardapp.hackschoolchat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.airbnb.epoxy.EpoxyModel
import com.stocardapp.hackschoolchat.backend.Backend
import com.stocardapp.hackschoolchat.chats.ChatViewModel_
import com.stocardapp.hackschoolchat.database.ChatMessage
import kotlinx.coroutines.experimental.coroutineScope
import retrofit2.Response
import timber.log.Timber

class ChatViewModel(context: Application) : AndroidViewModel(context) {

    // TODO: set dynamically
    private var name: String? = null

    fun chats(): LiveData<List<EpoxyModel<*>>> {
        return Transformations.map(ChatApplication.database.chatDao().getAllLive()) { input: List<ChatMessage> ->
            Timber.i("Got new set of models ...")
            input.map {
                val model = ChatViewModel_()
                model.id(it.timestamp)
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
        ChatApplication.database.chatDao().insert(chatMessage)
        Backend.instance.post(chatMessage).execute()
    }


}