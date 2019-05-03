package com.mashazavolnyuk.musicwavejava.albums;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.mashazavolnyuk.musicwavejava.data.Album;
import com.mashazavolnyuk.musicwavejava.model.AlbumModel;


import java.lang.ref.WeakReference;
import java.util.List;

public class AlbumListViewModel extends ViewModel {

    private MutableLiveData<List<Album>> songsLiveData;
    private AlbumModel albumModel;

    LiveData<List<Album>> getAlbums(Application application) {
        WeakReference<Application> contextWeakReference = new WeakReference<>(application);
        albumModel = new AlbumModel(contextWeakReference.get());
        if (songsLiveData == null) {
            songsLiveData = new MutableLiveData<>();
            loadAlbums();
        }
        return songsLiveData;
    }

    private void loadAlbums() {
        albumModel.loadAllAlbums(albums -> songsLiveData.postValue(albums));
    }
    public Album getAlbumByIndex(int index){
        return songsLiveData.getValue().get(index);
    }
}
