package com.mashazavolnyuk.musicwavejava.songList;

import com.mashazavolnyuk.musicwavejava.model.Song;

public interface IMusicProviderControl {

    void play(Song song);

    void play();

    void stop();

    void resume();

    void seekTo(long duration);
}
