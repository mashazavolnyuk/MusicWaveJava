package com.mashazavolnyuk.musicwavejava.adpater;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.albums.AlbumDetail;
import com.mashazavolnyuk.musicwavejava.data.Album;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AlbumsAdapter extends MusicAdapter<AlbumsAdapter.AlbumHolder> {

    private List<Album> albumList;
    private Activity activity;

    public AlbumsAdapter(@NonNull List<Album> albumList, Activity activity, int layout) {
        super(layout);
        this.albumList = albumList;
        size = albumList.size();
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
        holder.albumName.setText(albumList.get(position).getTitle());
        holder.linearLayout.setOnClickListener(view -> showDetail(albumList.get(position)));
        Uri uri = MusicUtil.getMediaStoreAlbumCoverUri(albumList.get(position).getId());
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.ic_music_note)
                .error(R.drawable.ic_music_note)
                .fit()
                .centerInside()
                .into(holder.imageViewCover);
    }


    private void showDetail(Album album) {
        Intent intent = new Intent(activity, AlbumDetail.class);
        intent.putExtra(AlbumDetail.EXTRA_ALBUM_ID, album);
        activity.startActivity(intent);
    }

    @Override
    protected AlbumsAdapter.AlbumHolder createViewHolder(View view) {
        return new AlbumsAdapter.AlbumHolder(view);
    }

    class AlbumHolder extends RecyclerView.ViewHolder {

        TextView albumName;
        CircleImageView imageViewCover;
        LinearLayout linearLayout;

        AlbumHolder(View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.nameAlbum);
            imageViewCover = itemView.findViewById(R.id.coverAlbum);
            linearLayout = itemView.findViewById(R.id.album_parent);

        }
    }
}
