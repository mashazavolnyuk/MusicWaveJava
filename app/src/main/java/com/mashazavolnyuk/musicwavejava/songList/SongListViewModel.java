package com.mashazavolnyuk.musicwavejava.songList;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.model.SongModel;
import java.lang.ref.WeakReference;
import java.util.List;

class SongListViewModel extends ViewModel {

    private MutableLiveData<List<Song>> songsLiveData;
    private WeakReference<Application> contextWeakReference;
    private SongModel songModel;

    LiveData<List<Song>> getSongs(Application application) {
        contextWeakReference = new WeakReference<>(application);
        songModel = new SongModel(contextWeakReference.get());
        if (songsLiveData == null) {
            songsLiveData = new MutableLiveData<>();
            loadSongs();
        }
        return songsLiveData;
    }

    private void loadSongs() {
        songModel.loadAllSongs(songs -> songsLiveData.postValue(songs));
    }
}
