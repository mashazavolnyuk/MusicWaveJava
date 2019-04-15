package com.mashazavolnyuk.musicwavejava.songList;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mashazavolnyuk.musicwavejava.AbsPlayerFragment;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.adpater.CustomTouchListener;
import com.mashazavolnyuk.musicwavejava.adpater.SongsAdapter;
import com.mashazavolnyuk.musicwavejava.helper.MusicPlayerRemote;
import java.util.Collections;
import java.util.Objects;

public class SongsListFragment extends AbsPlayerFragment {

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
                getApplication()).observe(this, songs -> {
                    if (songs != null) {
                        Collections.sort(songs, (a, b) -> a.getTitle().compareTo(b.getTitle()));
                        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerViewSongs.setAdapter(new SongsAdapter(songs));
                        recyclerViewSongs.addOnItemTouchListener(new CustomTouchListener(SongsListFragment.this.getActivity(),
                                (view, index) -> MusicPlayerRemote.playSongAt(index)));
                    }
                });
    }
}
