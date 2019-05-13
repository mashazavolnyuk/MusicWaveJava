package com.mashazavolnyuk.musicwavejava.palyingControlScreen;

import android.animation.ArgbEvaluator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.AbsPlayerFragment;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.helper.MusicPlayerRemote;
import com.mashazavolnyuk.musicwavejava.util.ImageUtil;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


public class CardPlayerFragment extends AbsPlayerFragment {

    private Unbinder unbinder;

    @BindView(R.id.artistName)
    TextView artistNameText;

    @BindView(R.id.songTitle)
    TextView songTitleText;

    @BindView(R.id.cover)
    CircleImageView coverView;

    @BindView(R.id.coverAnimation)
    CircleImageView coverAnimation;

    @BindView(R.id.layoutCover)
    RelativeLayout relativeLayout;

    ValueAnimator animator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_player, container, false);
        unbinder = ButterKnife.bind(this, view);
        relativeLayout.setClipToOutline(true);
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


    private void updateCurrentSong() {
        Song song = MusicPlayerRemote.getCurrentSong();
        if (song == null) return;
        artistNameText.setText(song.getArtistName());
        songTitleText.setText(song.getTitle());
        Uri uri = MusicUtil.getMediaStoreAlbumCoverUri(song.albumId);
        Picasso.get().load(uri)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        coverView.setImageBitmap(bitmap);
                        createPaletteAsync(bitmap);
                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        coverView.setImageBitmap(null);
                        coverView.setBackground(ImageUtil.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_music_note));
                        coverAnimation.setBackground(null);
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    public void createPaletteAsync(Bitmap bitmap) {
        Palette.from(bitmap).generate(palette -> {
            if (palette != null) {
                int colorStart = palette.getDominantColor(Color.BLACK);
                int colorEnd = palette.getLightVibrantColor(Color.WHITE);
                changeColorScheme(colorStart, colorEnd);
            }
        });
    }

    public void changeColorScheme(int start, int end) {
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{start, end});
        startAnimation(gradientDrawable, start, end);
    }

    public void startAnimation(GradientDrawable gradientDrawable, int start, int end) {
        coverAnimation.setBackground(null);
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        final GradientDrawable finalGradientDrawable1;
        final ArgbEvaluator evaluator = new ArgbEvaluator();
        if (gradientDrawable == null) {
            finalGradientDrawable1 = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{Color.parseColor("#FDB72B"), Color.parseColor("#88FDB72B")});
        } else {
            finalGradientDrawable1 = gradientDrawable;
        }
        animator = TimeAnimator.ofFloat(0.0f, 5.0f);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(valueAnimator -> {
            Float fraction = valueAnimator.getAnimatedFraction();
            int newStart = (int) evaluator.evaluate(fraction, start, end);
            int newEnd = (int) evaluator.evaluate(fraction, end, start);
            int[] newArray = {newStart, newEnd, newStart};
            finalGradientDrawable1.setColors(newArray);

        });
        coverAnimation.setBackground(finalGradientDrawable1);
        animator.start();
    }
}
