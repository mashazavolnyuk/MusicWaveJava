package com.mashazavolnyuk.musicwavejava;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mashazavolnyuk.musicwavejava.songList.SongsListFragment;

public class MainActivity extends SlidingMusicPanelActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate()");
    }

    @Override
    void makeContent() {
        toSongsList();
    }

    @Override
    protected View createContentView() {
        @SuppressLint("InflateParams")
        View contentView = getLayoutInflater().inflate(R.layout.activity_main_drawer_layout, null);
        ViewGroup drawerContent = contentView.findViewById(R.id.drawer_content_container);
        drawerContent.addView(wrapSlidingMusicPanel(R.layout.activity_main_content));
        return contentView;
    }

    private void toSongsList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        MainFragmentNavigation fragmentNavigation = new MainFragmentNavigation();
        fragmentManager.beginTransaction().replace(R.id.content, fragmentNavigation).commit();
    }
}
