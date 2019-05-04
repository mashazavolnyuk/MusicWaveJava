package com.mashazavolnyuk.musicwavejava.musicService;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import java.io.IOException;

public class Player implements Playback, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private Context context;

    private boolean isInitialized;

    private Playback.PlaybackCallbacks callbacks;

    private String dataPath;

    public Player(Context context) {
        this.context = context;
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
    }

    @Override
    public boolean pause() {
        mediaPlayer.pause();
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
            this.dataPath = dataPath;
            mediaPlayer.prepare();
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
        return mediaPlayer.isPlaying();
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
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (dataPath!= null && callbacks != null) {
            callbacks.onTrackEnded();
        }
    }
}



