package com.mashazavolnyuk.musicwavejava;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.mashazavolnyuk.musicwavejava.db.AppDatabase;

public class App extends Application {

    public static App instance;

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
