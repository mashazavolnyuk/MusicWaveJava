package com.mashazavolnyuk.musicwavejava.musicService;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import java.io.IOException;

public class Player implements Playback {

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private Context context;

    private boolean isInitialized;

    public Player(Context context) {
        this.context = context;
        isInitialized = true;
    }


    @Override
    public boolean play() {
        mediaPlayer.start();
        return true;
    }

    @Override
    public void stop() {
        mediaPlayer.reset();
        isInitialized = false;
    }

    @Override
    public boolean pause() {
        mediaPlayer.start();
        return true;
    }

    @Override
    public void setDataPath(@NonNull String dataPath) {
        if(isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        try {
            mediaPlayer.setDataSource(dataPath);
            mediaPlayer.prepareAsync();
            isInitialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public boolean isPlaying() {
        return isInitialized && mediaPlayer.isPlaying();
    }

    @Override
    public int duration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int position() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public int seek(int whereto) {
        mediaPlayer.seekTo(whereto);
        return whereto;
    }
}



