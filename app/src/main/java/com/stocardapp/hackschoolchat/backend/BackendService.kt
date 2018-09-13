package com.stocardapp.hackschoolchat.backend

import com.stocardapp.hackschoolchat.database.ChatMessage
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface BackendService {

    @GET("/")
    fun getMessages(): Call<List<ChatMessage>>

    @POST("/message")
    fun post(@Body chatMessage: ChatMessage): Call<ChatMessage>

}