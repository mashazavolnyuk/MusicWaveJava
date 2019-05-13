package com.mashazavolnyuk.musicwavejava.songList;

import android.arch.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.mashazavolnyuk.musicwavejava.AbsPlayerFragment;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.adpater.SongsAdapter;
import com.mashazavolnyuk.musicwavejava.helper.NavigationHelper;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SongsListFragment extends AbsPlayerFragment {

    private Unbinder unbinder;
    private RecyclerView recyclerViewSongs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, view);
        recyclerViewSongs = view.findViewById(R.id.recycler_view);
        fillSongsData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void fillSongsData() {
        SongListViewModel model = ViewModelProviders.of(this).get(SongListViewModel.class);
        model.getSongs(Objects.requireNonNull(getActivity()).
                getApplication()).observe(this, songs -> {
            if (songs != null) {
                recyclerViewSongs.setAdapter(new SongsAdapter(songs, R.layout.item_song));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.equalizer:
                NavigationHelper.openEqualizer(Objects.requireNonNull(getActivity()));
                return true;
            case R.id.search:
                NavigationHelper.startSearch(getActivity());
                return true;
            default:
                break;
        }

        return false;
    }
}
