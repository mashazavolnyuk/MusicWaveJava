package com.mashazavolnyuk.musicwavejava.albums;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mashazavolnyuk.musicwavejava.AbsPlayerFragment;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.adpater.SongsAdapter;
import com.mashazavolnyuk.musicwavejava.data.Album;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentDetailAlbum extends AbsPlayerFragment {

    private Unbinder unbinder;
    @BindView(R.id.song_list)
    public RecyclerView recyclerViewSongs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        Album album = getArguments().getParcelable(AlbumDetail.EXTRA_ALBUM_ID);
        recyclerViewSongs = view.findViewById(R.id.song_list);
        recyclerViewSongs.setAdapter(new SongsAdapter(album.songs,R.layout.item_song));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
