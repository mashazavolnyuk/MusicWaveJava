package com.mashazavolnyuk.musicwavejava.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mashazavolnyuk.musicwavejava.data.Song;

import java.util.List;

@Dao
public interface SongDao {

    @Query("SELECT * FROM song")
    List<Song> getAll();

    @Query("SELECT * FROM song WHERE id = :id")
    Song getById(long id);

    @Query("DELETE FROM song")
    void cleanAll();

    @Insert
    void insert(Song song);

    @Update
    void update(Song song);

    @Delete
    void delete(Song song);
}
