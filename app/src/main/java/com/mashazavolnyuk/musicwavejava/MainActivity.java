package com.mashazavolnyuk.musicwavejava;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.songList.IMusicProviderControl;
import com.mashazavolnyuk.musicwavejava.songList.SongsListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SlidingMusicPanelActivity implements IMusicProviderControl {

    final int MY_PERMISSIONS_REQUEST = 1;
    boolean isCanStartService;

    private MusicService player;
    boolean serviceBound = false;

    public final static String BROADCAST_ACTION_MUSIC = "com.mashazavolnyuk.musicwavejava.mainactivity";
    BroadcastReceiver br;
    List<IMusicStateListener> iMusicStateListenerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate()");
//        setContentView(R.layout.activity_main_content);
        iMusicStateListenerList = new ArrayList<>();
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


    private void registerRecevierMusicAction() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra("MusicState", 0);
                switch (status) {
                    case IMusicState.PLAY:
                        Toast.makeText(MainActivity.this, "Play", Toast.LENGTH_LONG).show();
                        notifyListeners(IMusicState.PLAY);
                        break;
                    case IMusicState.PAUSE:
                        Toast.makeText(MainActivity.this, "Pause", Toast.LENGTH_LONG).show();
                        notifyListeners(IMusicState.PAUSE);
                        break;
                    case IMusicState.STOP:
                        Toast.makeText(MainActivity.this, "STOP", Toast.LENGTH_LONG).show();
                        notifyListeners(IMusicState.STOP);
                        break;
                }
            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION_MUSIC);
        registerReceiver(br, intFilt);
    }

    private synchronized void notifyListeners(int musicState) {
        for (IMusicStateListener musicStateListener : iMusicStateListenerList) {
            musicStateListener.update(musicState);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
//            player = binder.getService();
            serviceBound = true;
            Toast.makeText(MainActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    private void playAudio(String media) {
        //Check is service is active
        if (!serviceBound) {
            Intent playerIntent = new Intent(this, MusicService.class);
            playerIntent.putExtra("media", media);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }

    private void toSongsList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SongsListFragment fragmentCountriesList = new SongsListFragment();
//        iMusicStateListenerList.add(fragmentCountriesList);
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
            registerRecevierMusicAction();
        }
        toSongsList();
        registerRecevierMusicAction();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                isCanStartService = grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void play(Song song) {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        intent.putExtra("media", song.data);
        intent.setAction(MusicService.ACTION_PLAY);
        startService(intent);
    }

    @Override
    public void play() {

    }

    @Override
    public void stop() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        intent.setAction(MusicService.ACTION_STOP);
        startService(intent);
    }

    @Override
    public void resume() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        intent.setAction(MusicService.ACTION_RESUME);
        startService(intent);
    }

    @Override
    public void seekTo(long duration) {

    }
}
