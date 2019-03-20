package com.mashazavolnyuk.musicwavejava.songList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.adpater.SongsAdapter;
import com.mashazavolnyuk.musicwavejava.model.Song;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SongsListFragment extends Fragment {

    private RecyclerView recyclerViewSongs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        recyclerViewSongs = view.findViewById(R.id.song_list);
        fillSongsData();
        return view;
    }

    private void fillSongsData() {
        SongListViewModel model = ViewModelProviders.of(this).get(SongListViewModel.class);
        model.getSongs(Objects.requireNonNull(getActivity()).
                getApplication()).observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(@Nullable List<Song> songs) {
                if (songs != null) {
                    Collections.sort(songs, new Comparator<Song>() {
                        public int compare(Song a, Song b) {
                            return a.getTitle().compareTo(b.getTitle());
                        }
                    });
                    recyclerViewSongs.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerViewSongs.setAdapter(new SongsAdapter(songs));
                }
            }
        });
    }
}
