package com.stocard.coolchat.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messages: Message)

    @Query("SELECT * FROM messages")
    fun getAll(): LiveData<List<Message>>

    @Query("DELETE FROM messages WHERE timestamp_millis = :timestamp")
    fun deleteByTimestamp(timestamp: Long)

    @Delete
    fun delete(message: Message)

    @Query("DELETE FROM messages")
    fun nukeTable()
}