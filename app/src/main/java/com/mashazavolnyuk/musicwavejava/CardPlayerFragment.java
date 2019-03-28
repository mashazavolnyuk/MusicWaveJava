package com.mashazavolnyuk.musicwavejava;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.FrameLayout;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CardPlayerFragment extends AbsPlayerFragment {

    private Unbinder unbinder;

    @Nullable
    @BindView(R.id.toolbar_container)
    FrameLayout toolbarContainer;
    @BindView(R.id.player_toolbar)
    Toolbar toolbar;
    @BindView(R.id.color_background)
    View colorBackground;

    private CardPlayerPlaybackControlsFragment playbackControlsFragment;
    private LinearLayoutManager layoutManager;


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
        layoutManager = null;
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


    @SuppressWarnings("ConstantConditions")
    private void updateCurrentSong() {
        //MusicPlayerRemote.getCurrentSong();
    }

    private void setUpSubFragments() {
        playbackControlsFragment = (CardPlayerPlaybackControlsFragment) getChildFragmentManager().findFragmentById(R.id.playback_controls_fragment);
    }
}
