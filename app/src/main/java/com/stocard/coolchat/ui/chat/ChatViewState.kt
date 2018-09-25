package com.stocard.coolchat.ui.chat

import com.airbnb.epoxy.EpoxyModel
import com.stocard.coolchat.data.NetworkState

data class ChatViewState(
        val networkState: NetworkState,
        val models: List<EpoxyModel<*>>
)