package com.mashazavolnyuk.musicwavejava;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.Build;

import com.mashazavolnyuk.musicwavejava.db.AppDatabase;
import com.mashazavolnyuk.musicwavejava.shortcurts.DynamicShortcutManager;

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
        // Set up dynamic shortcuts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            new DynamicShortcutManager(this).initDynamicShortcuts();
        }
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
