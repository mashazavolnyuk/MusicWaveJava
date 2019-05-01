package com.mashazavolnyuk.musicwavejava.loader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mashazavolnyuk.musicwavejava.data.Album;
import com.mashazavolnyuk.musicwavejava.data.Song;

import java.util.ArrayList;
import java.util.Collections;

public class AlbumLoader {

    public static ArrayList<Album> getAlbums(@NonNull Context context) {
        ArrayList<Album> albums = new ArrayList<>();
        ArrayList<Song> songs = SongLoader.getSongList(context);
        return splitIntoAlbums(songs);
    }

    @NonNull
    private static ArrayList<Album> splitIntoAlbums(@Nullable final ArrayList<Song> songs) {
        ArrayList<Album> albums = new ArrayList<>();
        if (songs != null) {
            for (Song song : songs) {
                getOrCreateAlbum(albums, song.albumId).songs.add(song);
            }
        }
        for (Album album : albums) {
            sortSongsByTrackNumber(album);
        }
        return albums;
    }

    private static Album getOrCreateAlbum(ArrayList<Album> albums, int albumId) {
        for (Album album : albums) {
            if (!album.songs.isEmpty() && album.songs.get(0).albumId == albumId) {
                return album;
            }
        }
        Album album = new Album();
        albums.add(album);
        return album;
    }

    private static void sortSongsByTrackNumber(Album album) {
        Collections.sort(album.songs, (o1, o2) -> o1.trackNumber - o2.trackNumber);
    }
}
