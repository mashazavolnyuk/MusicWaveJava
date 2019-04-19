package com.mashazavolnyuk.musicwavejava.shortcurts;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.mashazavolnyuk.musicwavejava.musicService.MusicService;

public class AppShortcutLauncherActivity extends Activity {

    public static final String KEY_SHORTCUT_TYPE = "com.mashazavolnyuk.musicwavejava.shortcurts";

    public static final int SHORTCUT_TYPE_SHUFFLE_ALL = 0;

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int shortcutType = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shortcutType = extras.getInt(KEY_SHORTCUT_TYPE, 0);
        }

        switch (shortcutType) {
            case SHORTCUT_TYPE_SHUFFLE_ALL:
                startService(MusicService.SHUFFLE_MODE_SHUFFLE);
                DynamicShortcutManager.reportShortcutUsed(this, ShuffleAllShortcut.getId());
                break;
        }
        finish();
    }

    private void startService(int shuffleMode) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.ACTION_PLAY);
        Bundle bundle = new Bundle();
        bundle.putInt(MusicService.INTENT_EXTRA_SHUFFLE_MODE, shuffleMode);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }


}
