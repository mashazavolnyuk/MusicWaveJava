package com.mashazavolnyuk.musicwavejava.adpater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.helper.MusicPlayerRemote;

import java.util.List;

public class MusicAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private int itemLayout;
    protected int size;

    MusicAdapter(int itemLayout) {
        this.itemLayout = itemLayout;
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return createViewHolder(view);
    }

    protected V createViewHolder(View view) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        //implement by sub
    }

    @Override
    public int getItemCount() {
        return size;
    }

    void playMusic(List<Song> songs, int startPosition) {
        MusicPlayerRemote.setSongs(songs);
        MusicPlayerRemote.playSongAt(startPosition);
    }
}
