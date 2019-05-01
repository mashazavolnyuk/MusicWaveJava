package com.mashazavolnyuk.musicwavejava.model;
import android.content.Context;
import com.mashazavolnyuk.musicwavejava.data.Album;
import com.mashazavolnyuk.musicwavejava.loader.AlbumLoader;

import java.util.ArrayList;
import java.util.List;

public class AlbumModel {

    private Context context;
    private List<Album> albums;

    public AlbumModel(Context context) {
        this.context = context;
        albums = new ArrayList<>();
    }

    public void loadAllAlbums(AlbumLoaderListener albumLoaderListener) {
        albums = AlbumLoader.getAlbums(context);
        albumLoaderListener.accept(albums);
    }

    public interface AlbumLoaderListener{
        void accept (List<Album> songs);
    }

}
