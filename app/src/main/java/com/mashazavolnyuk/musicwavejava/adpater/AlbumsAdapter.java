package com.mashazavolnyuk.musicwavejava.adpater;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.data.Album;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumHolder>  {

    private List<Album> albumList;

    public AlbumsAdapter(List<Album> albumList){
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album, parent, false);
        return new AlbumsAdapter.AlbumHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
        holder.albumName.setText(albumList.get(position).getTitle());
        Uri uri = MusicUtil.getMediaStoreAlbumCoverUri(albumList.get(position).getId());
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
        return albumList.size();
    }

    public class AlbumHolder extends RecyclerView.ViewHolder {

        TextView albumName;
        CircleImageView imageViewCover;

        public AlbumHolder(View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.nameAlbum);
            imageViewCover = itemView.findViewById(R.id.coverAlbum);

        }
    }
}
