package com.mashazavolnyuk.musicwavejava.albums;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.SlidingMusicPanelActivity;

public class AlbumDetail extends SlidingMusicPanelActivity {

    public static final String EXTRA_ALBUM_ID = "extra_album_id";

    @Override
    protected View createContentView() {
        @SuppressLint("InflateParams")
        View contentView = getLayoutInflater().inflate(R.layout.activity_main_drawer_layout, null);
        ViewGroup drawerContent = contentView.findViewById(R.id.drawer_content_container);
        drawerContent.addView(wrapSlidingMusicPanel(R.layout.activity_main_content));
        return contentView;
    }

    @Override
    public void makeContent() {
        showDetailAlbum();
    }

    private void showDetailAlbum(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentDetailAlbum fragmentNavigation = new FragmentDetailAlbum();
        Bundle extras = getIntent().getExtras();
        fragmentNavigation.setArguments(extras);
        fragmentManager.beginTransaction().replace(R.id.content, fragmentNavigation).commit();
    }
}
