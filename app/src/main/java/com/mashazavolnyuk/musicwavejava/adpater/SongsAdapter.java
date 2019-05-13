package com.mashazavolnyuk.musicwavejava.adpater;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongsAdapter extends MusicAdapter<SongsAdapter.SongHolder> {

    private List<Song> songs;

    public SongsAdapter(@NonNull List<Song> songs, int itemLayout) {
        super(itemLayout);
        this.songs = songs;
        size = songs.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapter.SongHolder holder, int position) {
        holder.titleSongText.setText(songs.get(position).getTitle());
        holder.artistSongText.setText(songs.get(position).getArtistName());
        holder.relativeLayout.setOnClickListener(view -> playMusic(songs, position));
        Uri uri = MusicUtil.getMediaStoreAlbumCoverUri(songs.get(position).albumId);
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.ic_music_note)
                .error(R.drawable.ic_music_note)
                .fit()
                .centerInside()
                .into(holder.imageViewCover);
    }

    @Override
    protected SongsAdapter.SongHolder createViewHolder(View view) {
        return new SongsAdapter.SongHolder(view);
    }

    class SongHolder extends RecyclerView.ViewHolder {
        TextView titleSongText;
        TextView artistSongText;
        ImageView imageViewCover;
        RelativeLayout relativeLayout;

        SongHolder(View itemView) {
            super(itemView);
            titleSongText = itemView.findViewById(R.id.title);
            artistSongText = itemView.findViewById(R.id.subTitle);
            imageViewCover = itemView.findViewById(R.id.cover);
            relativeLayout = itemView.findViewById(R.id.parent_item_music);
        }

    }
}
