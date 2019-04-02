package com.mashazavolnyuk.musicwavejava.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Song {

    public static final Song EMPTY_SONG = new Song(-1, "", -1, -1, -1, "", -1, -1, "", -1, "");

    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final String title;
    public final int trackNumber;
    public final int year;
    public final long duration;
    public final String data;
    public final long dateModified;
    public final int albumId;
    public final String albumName;
    public final int artistId;
    public final String artistName;


    public Song(int id, String title, int trackNumber, int year, long duration, String data, long dateModified, int albumId, String albumName, int artistId, String artistName) {
        this.id = id;
        this.title = title;
        this.trackNumber = trackNumber;
        this.year = year;
        this.duration = duration;
        this.data = data;
        this.dateModified = dateModified;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }
}
