package com.stocard.coolchat.backend

import com.stocard.coolchat.data.Message
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BackendService {

    @GET("/")
    fun fetchMessages(): Deferred<List<Message>>

    @POST("/message")
    fun postMessage(@Body chatMessage: Message): Deferred<Message>

}