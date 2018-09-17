package com.stocardapp.hackschoolchat.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ChatDao {

    @Query("SELECT * FROM chats")
    fun getAllLive(): LiveData<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messages: ChatMessage)

    @Query("DELETE FROM chats WHERE timestamp = :timestamp")
    fun deleteByTimestamp(timestamp: Long)

    @Delete
    fun delete(user: ChatMessage)

    @Query("DELETE FROM chats")
    fun nukeTable()
}
