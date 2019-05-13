package com.mashazavolnyuk.musicwavejava.albums;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mashazavolnyuk.musicwavejava.AbsPlayerFragment;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.adpater.AlbumsAdapter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentAlbums extends AbsPlayerFragment {


    private Unbinder unbinder;
    @BindView(R.id.rValbums)
    public RecyclerView recyclerViewSongs;
    AlbumListViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        unbinder = ButterKnife.bind(this, view);
        fillAlbumsData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void fillAlbumsData() {
        model = ViewModelProviders.of(this).get(AlbumListViewModel.class);
        model.getAlbums(Objects.requireNonNull(getActivity()).
                getApplication()).observe(this, albums -> {
            if (albums != null) {
                recyclerViewSongs.setAdapter(new AlbumsAdapter(albums, getActivity(), R.layout.item_song));
            }
        });
    }
}
