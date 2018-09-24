package com.stocard.coolchat.ui.chat

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import com.airbnb.epoxy.EpoxyModel
import com.stocard.coolchat.data.ChatRepository
import com.stocard.coolchat.data.NetworkState
import java.util.*
import java.util.concurrent.TimeUnit

class ChatViewModel(context: Application) : AndroidViewModel(context) {

    private val chatRepository = ChatRepository()

    val viewState: LiveData<ChatViewState>
        get() {
            val initialState = ChatViewState(
                    messages = emptyList(),
                    networkState = NetworkState.DONE
            )

            val combined = MediatorLiveData<ChatViewState>()
            combined.addSource(getMessages()) { messages ->
                val currentState = combined.value ?: initialState
                if (messages != null) combined.value = currentState.copy(messages = messages)
            }

            combined.addSource(chatRepository.networkState()) { state ->
                val currentState = combined.value ?: initialState
                if (state != null) combined.value = currentState.copy(networkState = state)
            }

            return combined
        }

    private fun getMessages(): LiveData<List<EpoxyModel<*>>> {
        return Transformations.map(chatRepository.chatMessages()) { chatMessages ->
            val modelList: MutableList<EpoxyModel<*>> = mutableListOf()
            var lastHeaderTs: Long = 0

            chatMessages.forEach {
                val fiveMinutesTs = it.timestamp / FIVE_MINUTES_MILLIS
                if (lastHeaderTs < fiveMinutesTs) {
                    val roundedTs = FIVE_MINUTES_MILLIS * fiveMinutesTs
                    val header = ChatHeaderViewModel_().apply {
                        value(Date(roundedTs))
                        id(it.timestamp)
                    }
                    modelList.add(header)
                    lastHeaderTs = fiveMinutesTs
                }

                val model = ChatMessageView_().apply {
                    id(it.timestamp)
                    name(it.name)
                    message(it.message)
                }
                modelList.add(model)
            }
            modelList
        }
    }

    fun send(text: String): LiveData<Boolean> {
        return chatRepository.sendMessage(text)
    }

    companion object {
        private val FIVE_MINUTES_MILLIS: Long = TimeUnit.MINUTES.toMillis(10)
    }
}