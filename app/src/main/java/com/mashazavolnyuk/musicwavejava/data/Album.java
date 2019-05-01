package com.mashazavolnyuk.musicwavejava.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Album implements Parcelable {

    public final List<Song> songs;

    public Album(List<Song> songs) {
        this.songs = songs;
    }

    public Album() {
        this.songs = new ArrayList<>();
    }

    protected Album(Parcel in) {
        this.songs = in.createTypedArrayList(Song.CREATOR);
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(songs);
    }
}
