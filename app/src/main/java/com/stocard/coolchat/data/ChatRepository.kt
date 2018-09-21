package com.stocard.coolchat.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.stocard.coolchat.backend.Backend
import com.stocard.coolchat.backend.BackendService
import kotlinx.coroutines.experimental.*
import java.util.concurrent.TimeUnit

class ChatRepository {

    private val backend: BackendService by lazy { Backend.instance }

    private val networkingState = MutableLiveData<NetworkState>()

    private val messages = object : LiveData<List<Message>>() {

        private var updater: Job? = null

        override fun onInactive() {
            super.onInactive()
            updater?.cancel()
        }

        override fun onActive() {
            super.onActive()
            updater?.cancel()
            updater = GlobalScope.launch {
                while (true) {
                    try {
                        networkingState.postValue(NetworkState.REFRESHING)
                        val messages = backend.fetchMessages().await()
                        postValue(messages)
                        networkingState.postValue(NetworkState.DONE)
                    } catch (ex: Exception) {
                        Log.e(LOG_TAG, "fetching messages failed with $ex")
                        networkingState.postValue(NetworkState.ERROR)
                    }
                    delay(2, TimeUnit.SECONDS)
                }
            }

        }
    }

    fun sendMessage(text: String): LiveData<Boolean> {
        val message = Message(
                message = text,
                name = "name",
                timestamp = System.currentTimeMillis()
        )
        val result = MutableLiveData<Boolean>()
        GlobalScope.launch {
            try {
                backend.postMessage(message).await()
                result.postValue(true)
            } catch (ex: Exception) {
                Log.e(LOG_TAG, "sending message failed with $ex")
                result.postValue(false)
            }
        }
        return result
    }


    fun chatMessages(): LiveData<List<Message>> {
        return messages
    }

    fun networkState(): LiveData<NetworkState> {
        return networkingState
    }

    companion object {
        private const val LOG_TAG = "ChatRepository"
    }

}