package com.stocardapp.hackschoolchat.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface ChatDao {
    @Query("SELECT * FROM chatmessage")
    List<ChatMessage> getAll();

    @Query("SELECT * FROM chatmessage")
    LiveData<List<ChatMessage>> getAllLive();

    @Query("SELECT * FROM chatmessage WHERE uid IN (:userIds)")
    List<ChatMessage> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM chatmessage WHERE name LIKE :name")
    ChatMessage findByName(String name);

    @Insert
    void insert(ChatMessage messages);

    @Insert
    void insertAll(List<ChatMessage> messages);

    @Delete
    void delete(ChatMessage user);

    @Query("DELETE FROM chatmessage")
    void nukeTable();
}
