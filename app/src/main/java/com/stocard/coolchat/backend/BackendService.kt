package com.stocard.coolchat.backend

import com.stocard.coolchat.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BackendService {

    @GET("/")
    fun fetchMessages(): Call<List<Message>>

    @POST("/message")
    fun postMessage(@Body chatMessage: Message): Call<Message>

}