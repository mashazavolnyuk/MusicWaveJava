package com.mashazavolnyuk.musicwavejava.player;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.AbsPlayerFragment;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.helper.MusicPlayerRemote;
import com.mashazavolnyuk.musicwavejava.util.ImageUtil;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CardPlayerFragment extends AbsPlayerFragment {

    private Unbinder unbinder;

    @BindView(R.id.artistName)
    TextView artistNameText;

    @BindView(R.id.songTitle)
    TextView songTitleText;

    @BindView(R.id.cover)
    ImageView coverView;

    @BindView(R.id.layoutCover)
    LinearLayout coverBlurView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_player, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onServiceConnected() {
        updateCurrentSong();
    }

    @Override
    public void onPlayingMetaChanged() {
        updateCurrentSong();
    }

    @Override
    public void onQueueChanged() {

    }

    @Override
    public void onMediaStoreChanged() {

    }

    private void updateCurrentSong() {
        Song song = MusicPlayerRemote.getCurrentSong();
        if(song==null) return;
        artistNameText.setText(song.getArtistName());
        songTitleText.setText(song.getTitle());
        Uri uri = MusicUtil.getMediaStoreAlbumCoverUri(song.albumId);
        Picasso.get()
                .load(uri)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                        /* Save the bitmap or do something with it here */

                        //Set it in the ImageView
                        coverView.setImageBitmap(bitmap);
                        Drawable d = new BitmapDrawable(getResources(), ImageUtil.blur(getActivity(),bitmap));
                        coverBlurView.setBackground(d);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }
}
