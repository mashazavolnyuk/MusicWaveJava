package com.mashazavolnyuk.musicwavejava.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mashazavolnyuk.musicwavejava.dao.SongDao;
import com.mashazavolnyuk.musicwavejava.data.Song;

@Database(entities = {Song.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SongDao songDao();
}
