package com.mashazavolnyuk.musicwavejava.search;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.model.SongModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SearchDataViewModel extends ViewModel {

    private MutableLiveData<List<Song>> songsLiveData;
    private SearchModel songModel;


    SearchDataViewModel(Application application) {
        WeakReference<Application> contextWeakReference = new WeakReference<>(application);
        songModel = new SearchModel(contextWeakReference.get());
        songsLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<List<Song>> loadSongs(String query) {
        if(query==null||query.isEmpty()){
            songsLiveData.postValue(null);
        }
        songModel.search(query, new SearchModel.SongLoaderListener() {
            @Override
            public void accept(List<Song> songs) {
                 songsLiveData.postValue(songs);
            }
        });
        return songsLiveData;
    }
}
