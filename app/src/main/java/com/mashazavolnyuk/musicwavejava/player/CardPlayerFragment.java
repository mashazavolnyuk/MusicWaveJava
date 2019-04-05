package com.mashazavolnyuk.musicwavejava.player;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.AbsPlayerFragment;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.helper.MusicPlayerRemote;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CardPlayerFragment extends AbsPlayerFragment {

    private Unbinder unbinder;

    @BindView(R.id.artistName)
    TextView artistNameText;

    @BindView(R.id.songTitle)
    TextView songTitleText;

    private CardPlayerPlaybackControlsFragment playbackControlsFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_player, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSubFragments();
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
        artistNameText.setText(song.getArtistName());
        songTitleText.setText(song.getTitle());
    }

    private void setUpSubFragments() {
        playbackControlsFragment = (CardPlayerPlaybackControlsFragment) getChildFragmentManager().findFragmentById(R.id.playback_controls_fragment);
    }
}
