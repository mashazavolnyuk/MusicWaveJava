package com.mashazavolnyuk.musicwavejava.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mashazavolnyuk.musicwavejava.data.Song;

import java.util.ArrayList;
import java.util.Collections;

public class SongLoader {

    private static final String[] BASE_SELECTION = new String[]{
            BaseColumns._ID,// 0
            MediaStore.Audio.AudioColumns.TITLE,// 1
            MediaStore.Audio.AudioColumns.TRACK,// 2
            MediaStore.Audio.AudioColumns.YEAR,// 3
            MediaStore.Audio.AudioColumns.DURATION,// 4
            MediaStore.Audio.AudioColumns.DATA,// 5
            MediaStore.Audio.AudioColumns.DATE_MODIFIED,// 6
            MediaStore.Audio.AudioColumns.ALBUM_ID,// 7
            MediaStore.Audio.AudioColumns.ALBUM,// 8
            MediaStore.Audio.AudioColumns.ARTIST_ID,// 9
            MediaStore.Audio.AudioColumns.ARTIST,// 10
    };

    //method to retrieve item_song info from device
    public static ArrayList<Song> getSongList(@NonNull Context context) {
        ArrayList<Song> songList = new ArrayList<>();
        //query external audio
        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicResolver.query(musicUri, BASE_SELECTION, null, null, null);
        //iterate over results if valid
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final int id = cursor.getInt(0);
                final String title = cursor.getString(1);
                final int trackNumber = cursor.getInt(2);
                final int year = cursor.getInt(3);
                final long duration = cursor.getLong(4);
                final String data = cursor.getString(5);
                final long dateModified = cursor.getLong(6);
                final int albumId = cursor.getInt(7);
                final String albumName = cursor.getString(8);
                final int artistId = cursor.getInt(9);
                final String artistName = cursor.getString(10);
                songList.add(new Song(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName, artistId, artistName));
            } while (cursor.moveToNext());
            cursor.close();
        }
        Collections.sort(songList, (a, b) -> a.getTitle().compareTo(b.getTitle()));
        return songList;
    }

    //method to retrieve item_song info from device
    public static ArrayList<Song> getSongList(@NonNull Context context, String selection, @NonNull String query) {
        ArrayList<Song> songList = new ArrayList<>();
        //query external audio
        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = makeSongCursor(context, selection + " LIKE ?", new String[]{"%" + query + "%"}, null);
        //iterate over results if valid
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final int id = cursor.getInt(0);
                final String title = cursor.getString(1);
                final int trackNumber = cursor.getInt(2);
                final int year = cursor.getInt(3);
                final long duration = cursor.getLong(4);
                final String data = cursor.getString(5);
                final long dateModified = cursor.getLong(6);
                final int albumId = cursor.getInt(7);
                final String albumName = cursor.getString(8);
                final int artistId = cursor.getInt(9);
                final String artistName = cursor.getString(10);
                songList.add(new Song(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName, artistId, artistName));
            } while (cursor.moveToNext());
            cursor.close();
        }
        Collections.sort(songList, (a, b) -> a.getTitle().compareTo(b.getTitle()));
        return songList;
    }

    @Nullable
    private static Cursor makeSongCursor(@NonNull final Context context, @Nullable String selection, String[] selectionValues, final String sortOrder) {
        try {
            return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    BASE_SELECTION, selection, selectionValues, sortOrder);
        } catch (SecurityException e) {
            return null;
        }
    }
}
