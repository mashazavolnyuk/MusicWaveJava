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

public class MainActivity extends SlidingMusicPanelActivity{

    final int MY_PERMISSIONS_REQUEST = 1;
    boolean isCanStartService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate()");
        init();
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
        SongsListFragment fragmentCountriesList = new SongsListFragment();
        fragmentManager.beginTransaction().replace(R.id.content, fragmentCountriesList).commit();
    }

    private void init() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                isCanStartService = false;
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            isCanStartService = true;
            toSongsList();
        }
        toSongsList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                isCanStartService = grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
