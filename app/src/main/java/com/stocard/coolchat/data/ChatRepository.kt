package com.stocard.coolchat.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.stocard.coolchat.backend.Backend
import com.stocard.coolchat.backend.BackendService
import kotlinx.coroutines.experimental.*
import java.util.concurrent.TimeUnit

class ChatRepository(context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val backend: BackendService by lazy { Backend.instance }
    private val nameState = MutableLiveData<String>()
    private val networkingState = MutableLiveData<NetworkState>()

    init {
        nameState.postValue(prefs.getString("name", null))
    }

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

    fun setName(name: String) {
        prefs.edit().putString("name", name).apply()
        nameState.postValue(name)
    }

    fun sendMessage(text: String): LiveData<Boolean> {
        val message = Message(
                message = text,
                name = nameState.value ?: "no-name",
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

    fun nameState(): LiveData<String> {
        return nameState
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