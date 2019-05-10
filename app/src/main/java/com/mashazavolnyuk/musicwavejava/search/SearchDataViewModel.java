package com.mashazavolnyuk.musicwavejava.search;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.lang.ref.WeakReference;

import java.util.List;

public class SearchDataViewModel extends ViewModel {

    private MutableLiveData<List<Object>> data;
    private SearchModel songModel;


    SearchDataViewModel(Application application) {
        WeakReference<Application> contextWeakReference = new WeakReference<>(application);
        songModel = new SearchModel(contextWeakReference.get());
        data = new MutableLiveData<>();

    }

    public MutableLiveData<List<Object>> loadSongs(String query) {
        if (query == null || query.isEmpty()) {
            data.postValue(null);
        }
        songModel.search(query, new SearchModel.SongLoaderListener() {
            @Override
            public void accept(List<Object> objects) {
                data.postValue(objects);
            }
        });
        return data;
    }
}
