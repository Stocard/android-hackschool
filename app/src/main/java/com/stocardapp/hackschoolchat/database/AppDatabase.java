package com.stocardapp.hackschoolchat.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessage.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ChatDao chatDao();
}
