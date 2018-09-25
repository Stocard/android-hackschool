package com.stocard.coolchat.data

data class Message(
        val message: String,
        val name: String,
        val timestamp: Long
) {

    override fun toString(): String {
        return "$name: $message"
    }
}