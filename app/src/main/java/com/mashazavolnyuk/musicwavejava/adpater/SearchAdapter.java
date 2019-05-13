package com.mashazavolnyuk.musicwavejava.adpater;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.albums.AlbumDetail;
import com.mashazavolnyuk.musicwavejava.data.Album;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.helper.MusicPlayerRemote;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static final int HEADER = 0;
    private static final int ALBUM = 1;
    private static final int SONG = 3;

    private final AppCompatActivity activity;

    private List<Object> dataSet;

    public SearchAdapter(@NonNull AppCompatActivity activity, @NonNull List<Object> dataSet) {
        this.activity = activity;
        this.dataSet = dataSet;
    }

    public void swapDataSet(@NonNull List<Object> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (dataSet.get(position) != null && dataSet.get(position) instanceof Song) {
            return SONG;
        }
        if (dataSet.get(position) instanceof Album) return ALBUM;
        return HEADER;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == HEADER) {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.sub_header, viewGroup, false), viewType);
        }
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_song, viewGroup, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER:
                holder.titleHeader.setText(dataSet.get(position).toString());
                break;
            case SONG:
                holder.titleSongText.setText(((Song) dataSet.get(position)).getTitle());
                holder.artistSongText.setText(((Song) dataSet.get(position)).getArtistName());
                Uri uri = MusicUtil.getMediaStoreAlbumCoverUri(((Song) dataSet.get(position)).albumId);
                Picasso.get()
                        .load(uri)
                        .placeholder(R.drawable.ic_music_note)
                        .error(R.drawable.ic_music_note)
                        .fit()
                        .centerInside()
                        .into(holder.imageViewCover);
                break;
            case ALBUM:
                holder.titleSongText.setText(((Album) dataSet.get(position)).getTitle());
                holder.artistSongText.setText(((Album) dataSet.get(position)).getArtistName());
                uri = MusicUtil.getMediaStoreAlbumCoverUri(((Album) dataSet.get(position)).getId());
                Picasso.get()
                        .load(uri)
                        .placeholder(R.drawable.ic_music_note)
                        .error(R.drawable.ic_music_note)
                        .fit()
                        .centerInside()
                        .into(holder.imageViewCover);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleSongText;
        TextView artistSongText;
        TextView titleHeader;
        ImageView imageViewCover;
        RelativeLayout relativeLayout;

        ViewHolder(@NonNull View itemView, int itemType) {
            super(itemView);
            itemView.setOnClickListener(this);
            switch (itemType) {
                case SONG:
                    titleSongText = itemView.findViewById(R.id.song_title);
                    artistSongText = itemView.findViewById(R.id.song_artist);
                    imageViewCover = itemView.findViewById(R.id.coverSong);
                    relativeLayout = itemView.findViewById(R.id.parent_item_music);
                    break;
                case ALBUM:
                    titleSongText = itemView.findViewById(R.id.song_title);
                    artistSongText = itemView.findViewById(R.id.song_artist);
                    imageViewCover = itemView.findViewById(R.id.coverSong);
                    relativeLayout = itemView.findViewById(R.id.parent_item_music);
                    break;
                default:
                    titleHeader = itemView.findViewById(R.id.title);

            }
        }

        @Override
        public void onClick(View view) {
            Object item = dataSet.get(getAdapterPosition());
            switch (getItemViewType()) {
                case ALBUM:
                    Intent intent = new Intent(activity, AlbumDetail.class);
                    intent.putExtra(AlbumDetail.EXTRA_ALBUM_ID, (Album) item);
                    activity.startActivity(intent);
                    break;
                case SONG:
                    ArrayList<Song> playList = new ArrayList<>();
                    playList.add((Song) item);
                    playMusic(playList);
                    break;
            }
        }

        void playMusic(List<Song> songs) {
            MusicPlayerRemote.setSongs(songs);
            MusicPlayerRemote.playSongAt(0);
        }
    }
}
