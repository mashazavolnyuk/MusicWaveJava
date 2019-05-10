package com.mashazavolnyuk.musicwavejava.search;

import android.content.Context;
import android.provider.MediaStore.Audio;

import com.mashazavolnyuk.musicwavejava.data.Album;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.loader.AlbumLoader;
import com.mashazavolnyuk.musicwavejava.loader.SongLoader;

import java.util.ArrayList;
import java.util.List;

public class SearchModel {

    private Context context;
    SearchModel(Context context) {
        this.context = context;
    }

    public void search(String query, SearchModel.SongLoaderListener songLoaderListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Object> objects = new ArrayList<>();
                List<Song>songList = SongLoader.getSongList(context, Audio.AudioColumns.TITLE, query);
                objects.add("Songs");
                objects.addAll(songList);
                List<Album> albums = AlbumLoader.getAlbums(context, Audio.AudioColumns.ALBUM, query);
                objects.add("Albums");
                objects.addAll(albums);
                songLoaderListener.accept(objects);
            }
        }).start();
    }

    public interface SongLoaderListener {
        void accept(List<Object> songs);
    }
}
