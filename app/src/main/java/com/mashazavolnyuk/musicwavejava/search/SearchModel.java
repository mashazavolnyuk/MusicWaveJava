package com.mashazavolnyuk.musicwavejava.search;

import android.content.Context;

import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.loader.SongLoader;
import com.mashazavolnyuk.musicwavejava.model.SongModel;

import java.util.ArrayList;
import java.util.List;

public class SearchModel {

    private Context context;
    private List<Song> songList;

    public SearchModel(Context context) {
        this.context = context;
        songList = new ArrayList<>();
    }

    public void search(String query, SearchModel.SongLoaderListener songLoaderListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                songList = SongLoader.getSongList(context,query);
                songLoaderListener.accept(songList);
            }
        }).start();
    }

    public interface SongLoaderListener {
        void accept(List<Song> songs);
    }
}
