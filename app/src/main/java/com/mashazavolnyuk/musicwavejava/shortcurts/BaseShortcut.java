package com.mashazavolnyuk.musicwavejava.shortcurts;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.os.Bundle;

public abstract class BaseShortcut {

    static final String ID_PREFIX = "com.mashazavolnyuk.musicwavejava.shortcurts.id.";

    Context context;

    BaseShortcut(Context context) {
        this.context = context;
    }

    static public String getId() {
        return ID_PREFIX + "invalid";
    }

    abstract ShortcutInfo getShortcutInfo();

    Intent getPlaySongsIntent() {
        Intent intent = new Intent(context, AppShortcutLauncherActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        Bundle b = new Bundle();
        b.putInt(AppShortcutLauncherActivity.KEY_SHORTCUT_TYPE, AppShortcutLauncherActivity.SHORTCUT_TYPE_SHUFFLE_ALL);
        intent.putExtras(b);
        return intent;
    }


}
