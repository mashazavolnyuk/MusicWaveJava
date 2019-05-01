package com.mashazavolnyuk.musicwavejava.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Album {

    public final List<Song> songs;

    public Album(List<Song> songs) {
        this.songs = songs;
    }

    public Album() {
        this.songs = new ArrayList<>();
    }

    public int getId() {
        return safeGetFirstSong().albumId;
    }

    public String getTitle() {
        return safeGetFirstSong().albumName;
    }

    public int getArtistId() {
        return safeGetFirstSong().artistId;
    }

    public String getArtistName() {
        return safeGetFirstSong().artistName;
    }

    public int getYear() {
        return safeGetFirstSong().year;
    }

    public long getDateModified() {
        return safeGetFirstSong().dateModified;
    }

    public int getSongCount() {
        return songs.size();
    }

    @NonNull
    public Song safeGetFirstSong() {
        return songs.isEmpty() ? Song.EMPTY_SONG : songs.get(0);
    }


}
