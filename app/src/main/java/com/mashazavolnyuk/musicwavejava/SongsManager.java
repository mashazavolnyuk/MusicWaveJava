package com.mashazavolnyuk.musicwavejava;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Objects;

public class SongsManager {

    private static ArrayList<Song> songsList = new ArrayList<>();
    private static String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
    };

    public static ArrayList<Song> getSongs(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor songCursor = contentResolver.query(songUri, projection, selection, null, null);
        Song.SongBuilder songBuilder = new Song.SongBuilder();
        if (songCursor != null && songCursor.moveToFirst()) {
            do {
                String currentName = songCursor.getString(4);
                String currentTitle = songCursor.getString(2);
                songBuilder.setName(currentName);
                songBuilder.setTitle(currentTitle);
                Song song = songBuilder.build();
                songsList.add(song);
            } while (songCursor.moveToNext());
        }
        Objects.requireNonNull(songCursor).close();
        return songsList;
    }
}
