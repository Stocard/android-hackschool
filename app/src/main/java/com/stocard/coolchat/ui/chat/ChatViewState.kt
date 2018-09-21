package com.stocard.coolchat.ui.chat

import com.stocard.coolchat.data.Message
import com.stocard.coolchat.data.NetworkState

data class ChatViewState(
        val networkState: NetworkState,
        val messages: List<Message>
)