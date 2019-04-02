package com.mashazavolnyuk.musicwavejava.model;

import android.content.Context;
import android.util.Log;

import com.mashazavolnyuk.musicwavejava.App;
import com.mashazavolnyuk.musicwavejava.dao.SongDao;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.db.AppDatabase;
import com.mashazavolnyuk.musicwavejava.loader.SongLoader;

import java.util.ArrayList;
import java.util.List;

public class SongModel {

    private Context context;
    private SongDao songDao;
    private List<Song> songList;
    String TAG = "SongModel";

    public SongModel(Context context) {
        this.context = context;
        songDao = getSongDao();
        songList = new ArrayList<>();
    }

    public void loadAllSongs(SongLoaderListener songLoaderListener) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                deleteAllSongsFromDB(new SongDeleteListener() {
//                    @Override
//                    public void done() {
//                        getAllSongsFromDB(songs -> {
//                            if(songs!=null && songs.size()!=0){
//                                songLoaderListener.accept(songs);
//                            }
//                            else {
                                songList = SongLoader.getSongList(context);
                                songLoaderListener.accept(songList);
                                // writeListSongsToDB(songList);
                          //  }
//                        });
//                    }
//                });
//            }
//        }).start();

    }

    private SongDao getSongDao() {
        AppDatabase db = App.getInstance().getDatabase();
        return db.songDao();
    }

    private void getAllSongsFromDB(SongLoaderListener songLoaderListener){
        new Thread(() -> songLoaderListener.accept( songDao.getAll())).start();
    }

    private void writeListSongsToDB(List<Song> songs) {
        new Thread(() -> {
            int i=0;
            for (Song song : songs) {
                i++;
                Log.d(TAG, "process insert id= "+ song.id + "index= "+ i);
                songDao.insert(song);
            }
        }).start();

    }

    private void deleteAllSongsFromDB(SongDeleteListener songDeleteListener) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               songDao.cleanAll();
               songDeleteListener.done();
           }
       }).start();
    }

    public interface SongLoaderListener{
       void accept (List<Song> songs);
    }

    public interface SongDeleteListener{
        void done ();
    }
}
