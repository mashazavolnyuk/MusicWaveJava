package com.mashazavolnyuk.musicwavejava.adpater;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongHolder> {

    private List<Song> songList;

    public SongsAdapter(List<Song> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongsAdapter.SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new SongHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapter.SongHolder holder, int position) {
        holder.titleSongText.setText(songList.get(position).getTitle());
        holder.artistSongText.setText(songList.get(position).getArtistName());
        Uri uri = MusicUtil.getMediaStoreAlbumCoverUri(songList.get(position).albumId);
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.ic_music_note)
                .error(R.drawable.ic_music_note)
                .fit()
                .centerInside()
                .into(holder.imageViewCover);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class SongHolder extends RecyclerView.ViewHolder {
        TextView titleSongText;
        TextView artistSongText;
        ImageView imageViewCover;

        SongHolder(View itemView) {
            super(itemView);
            titleSongText = itemView.findViewById(R.id.song_title);
            artistSongText = itemView.findViewById(R.id.song_artist);
            imageViewCover = itemView.findViewById(R.id.coverSong);
        }
    }
}
