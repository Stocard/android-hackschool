package com.stocardapp.hackschoolchat.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ChatDao {
    @Query("SELECT * FROM chats")
    fun getAll(): List<ChatMessage>

    @Query("SELECT * FROM chats")
    fun getAllLive(): LiveData<List<ChatMessage>>

    @Query("SELECT * FROM chats WHERE name LIKE :name")
    fun findByName(name: String): ChatMessage

    @Insert
    fun insert(messages: ChatMessage)

    @Insert
    fun insertAll(messages: List<ChatMessage>)

    @Delete
    fun delete(user: ChatMessage)

    @Query("DELETE FROM chats")
    fun nukeTable()
}
