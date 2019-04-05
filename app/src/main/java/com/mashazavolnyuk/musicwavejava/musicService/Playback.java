package com.mashazavolnyuk.musicwavejava.musicService;

import android.support.annotation.NonNull;

public interface Playback {

    boolean play();

    void stop();

    boolean pause();

    void setDataPath(@NonNull String dataPath);

    boolean isInitialized();

    boolean isPlaying();

    int duration();

    int position();

    int seek(int whereto);

    void setCallbacks(PlaybackCallbacks  callbacks);

    interface PlaybackCallbacks {

        void onTrackEnded();
    }
}
