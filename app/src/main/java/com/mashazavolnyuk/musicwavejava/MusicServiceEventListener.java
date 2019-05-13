package com.mashazavolnyuk.musicwavejava;

public interface MusicServiceEventListener {

    void onServiceConnected();

    void onServiceDisconnected();

    void onPlayingMetaChanged();

    void onPlayStateChanged();

    void onRepeatModeChanged();

    void onShuffleModeChanged();

}
