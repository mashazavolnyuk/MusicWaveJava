package com.mashazavolnyuk.musicwavejava.songList;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mashazavolnyuk.musicwavejava.loader.SongLoader;
import com.mashazavolnyuk.musicwavejava.model.Song;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class SongListViewModel extends ViewModel {

    private MutableLiveData<List<Song>> songs;
    private WeakReference<Application> contextWeakReference;

    LiveData<List<Song>> getSongs(Application application) {
        contextWeakReference = new WeakReference<>(application);
        if (songs == null) {
            songs = new MutableLiveData<>();
            songs.postValue(loadSongs());
        }
        return songs;
    }

    private ArrayList<Song> loadSongs() {
        return SongLoader.getSongList(Objects.requireNonNull(contextWeakReference.get()));
    }
}
