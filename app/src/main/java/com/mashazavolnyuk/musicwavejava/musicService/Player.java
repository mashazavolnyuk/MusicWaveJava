package com.mashazavolnyuk.musicwavejava.musicService;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import java.io.IOException;

public class Player implements Playback, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private Context context;

    private boolean isInitialized;

    private Playback.PlaybackCallbacks callbacks;

    public Player(Context context) {
        this.context = context;
        isInitialized = true;
        mediaPlayer.setOnCompletionListener(this);
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
        if (isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(dataPath);
            mediaPlayer.prepare();
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

    public void setCallbacks(Playback.PlaybackCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (callbacks != null) {
            callbacks.onTrackEnded();
        }
    }
}



