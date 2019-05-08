package com.mashazavolnyuk.musicwavejava.adpater;

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
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.helper.MusicPlayerRemote;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static final int HEADER = 0;
    private static final int ALBUM = 1;
    private static final int ARTIST = 2;
    private static final int SONG = 3;

    private final AppCompatActivity activity;

    private List<Song> dataSet;

    public SearchAdapter(@NonNull AppCompatActivity activity, @NonNull List<Song> dataSet) {
        this.activity = activity;
        this.dataSet = dataSet;
    }

    public void swapDataSet(@NonNull List<Song> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
//        if (dataSet.get(position) instanceof Album) return ALBUM;
//        if (dataSet.get(position) instanceof Artist) return ARTIST;
        if (dataSet.get(position) != null && dataSet.get(position) instanceof Song) {
            return SONG;
        }
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
            case SONG:
            holder.titleSongText.setText(dataSet.get(position).getTitle());
            holder.artistSongText.setText(dataSet.get(position).getArtistName());
            holder.relativeLayout.setOnClickListener(view -> playMusic(dataSet, position));
            Uri uri = MusicUtil.getMediaStoreAlbumCoverUri(dataSet.get(position).albumId);
            Picasso.get()
                    .load(uri)
                    .placeholder(R.drawable.ic_music_note)
                    .error(R.drawable.ic_music_note)
                    .fit()
                    .centerInside()
                    .into(holder.imageViewCover);
            break;
//            case ALBUM:
//                final Album album = (Album) dataSet.get(position);
//                holder.title.setText(album.getTitle());
//                holder.text.setText(MusicUtil.getAlbumInfoString(activity, album));
//                SongGlideRequest.Builder.from(Glide.with(activity), album.safeGetFirstSong())
//                        .checkIgnoreMediaStore(activity).build()
//                        .into(holder.image);
//                break;
//            case ARTIST:
//                final Artist artist = (Artist) dataSet.get(position);
//                holder.title.setText(artist.getName());
//                holder.text.setText(MusicUtil.getArtistInfoString(activity, artist));
//                ArtistGlideRequest.Builder.from(Glide.with(activity), artist)
//                        .build().into(holder.image);
//                break;
//            case SONG:
//                final Song song = (Song) dataSet.get(position);
//                holder.title.setText(song.title);
//                holder.text.setText(MusicUtil.getSongInfoString(song));
//                break;
//            default:
//                holder.title.setText(dataSet.get(position).toString());
//                break;
        }
    }

    void playMusic(List<Song> songs, int startPosition) {
        MusicPlayerRemote.setSongs(songs);
        MusicPlayerRemote.playSongAt(startPosition);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleSongText;
        TextView artistSongText;
        ImageView imageViewCover;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView, int itemViewType) {
            super(itemView);
            titleSongText = itemView.findViewById(R.id.song_title);
            artistSongText = itemView.findViewById(R.id.song_artist);
            imageViewCover = itemView.findViewById(R.id.coverSong);
            relativeLayout = itemView.findViewById(R.id.parent_item_music);

        }
    }
}
