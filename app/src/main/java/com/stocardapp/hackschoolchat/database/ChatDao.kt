package com.stocardapp.hackschoolchat.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ChatDao {

    @Query("SELECT * FROM chats")
    fun getAllLive(): LiveData<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(messages: ChatMessage)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(messages: List<ChatMessage>)

    @Delete
    fun delete(user: ChatMessage)

    @Query("DELETE FROM chats")
    fun nukeTable()
}
