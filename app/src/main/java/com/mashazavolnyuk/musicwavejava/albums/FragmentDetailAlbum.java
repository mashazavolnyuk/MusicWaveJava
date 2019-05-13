package com.mashazavolnyuk.musicwavejava.albums;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.AbsPlayerFragment;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.adpater.AlbumsAdapterDetail;
import com.mashazavolnyuk.musicwavejava.data.Album;
import com.mashazavolnyuk.musicwavejava.util.ImageUtil;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentDetailAlbum extends AbsPlayerFragment {

    private Unbinder unbinder;
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerViewSongs;
    @BindView(R.id.cover)
    public CircleImageView cover;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.subTitle)
    public TextView subTitle;

    private Album album;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        assert getArguments() != null;
        album = getArguments().getParcelable(AlbumDetailActivity.EXTRA_ALBUM_ID);
        if (album != null) {
            updateAlbumInfo();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateAlbumInfo() {
        title.setText(album.getTitle());
        subTitle.setText(album.getArtistName());
        assert album.songs != null;
        recyclerViewSongs.setAdapter(new AlbumsAdapterDetail(album.songs, R.layout.item_album));
        Uri uri = MusicUtil.getMediaStoreAlbumCoverUri(album.getId());
        Picasso.get().load(uri)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        cover.setImageBitmap(bitmap);

                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        cover.setBackground(ImageUtil.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_music_note));

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }
}
