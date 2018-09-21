package com.stocard.coolchat.ui.chat

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.stocard.coolchat.data.ChatRepository
import com.stocard.coolchat.data.NetworkState

class ChatViewModel(context: Application) : AndroidViewModel(context) {

    private val chatRepository = ChatRepository()

    val viewState: LiveData<ChatViewState>
        get() {
            val initialState = ChatViewState(
                    messages = emptyList(),
                    networkState = NetworkState.DONE
            )

            val combined = MediatorLiveData<ChatViewState>()
            combined.addSource(chatRepository.chatMessages()) { messages ->
                val currentState = combined.value ?: initialState
                if (messages != null) combined.value = currentState.copy(messages = messages)
            }

            combined.addSource(chatRepository.networkState()) { state ->
                val currentState = combined.value ?: initialState
                if (state != null) combined.value = currentState.copy(networkState = state)
            }

            return combined
        }

    fun send(text: String): LiveData<Boolean> {
        return chatRepository.sendMessage(text)
    }
}