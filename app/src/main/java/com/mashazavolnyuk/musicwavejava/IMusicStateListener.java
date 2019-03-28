package com.mashazavolnyuk.musicwavejava;

public interface IMusicStateListener {

    void update(int state);

    void updateProgress(long duration);
}
