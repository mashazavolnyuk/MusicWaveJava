package com.mashazavolnyuk.musicwavejava.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.mashazavolnyuk.musicwavejava.data.Song;

import java.util.ArrayList;

public class SongLoader {

    private static final String[] BASE_PROJECTION = new String[]{
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
        Cursor cursor = musicResolver.query(musicUri, BASE_PROJECTION, null, null, null);
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
        return songList;
    }
}
