package com.mashazavolnyuk.musicwavejava.adpater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;


import java.util.List;

public class AlbumsAdapterDetail extends MusicAdapter<AlbumsAdapterDetail.AlbumDetailHolder> {

    private List<Song> songs;

    public AlbumsAdapterDetail(@NonNull List<Song> albumList, int layout) {
        super(layout);
        this.songs = albumList;
        size = albumList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsAdapterDetail.AlbumDetailHolder holder, int position) {
        holder.title.setText(songs.get(position).getTitle());
        holder.parentLayout.setOnClickListener(view -> playMusic(songs, position));
        holder.clock.setText(MusicUtil.getReadableDurationString(songs.get(position).duration));

    }

    @Override
    protected AlbumsAdapterDetail.AlbumDetailHolder createViewHolder(View view) {
        return new AlbumsAdapterDetail.AlbumDetailHolder(view);
    }

    class AlbumDetailHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView clock;
        LinearLayout parentLayout;

        AlbumDetailHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            clock = itemView.findViewById(R.id.clock);
            parentLayout = itemView.findViewById(R.id.parent_item_music);
        }
    }
}
