package com.mashazavolnyuk.musicwavejava.shortcurts;

import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


@RequiresApi(api = Build.VERSION_CODES.N_MR1)
public class DynamicShortcutManager {

    private Context context;
    private ShortcutManager shortcutManager;


    public DynamicShortcutManager(Context context) {
        this.context = context;
        shortcutManager = this.context.getSystemService(ShortcutManager.class);
    }

    public void initDynamicShortcuts() {
        if (shortcutManager.getDynamicShortcuts().size() == 0) {
            shortcutManager.setDynamicShortcuts(getDefaultShortcuts());
        }
    }


    private List<ShortcutInfo> getDefaultShortcuts() {
        return (Collections.singletonList(
                new ShuffleAllShortcut(context).getShortcutInfo()
        ));
    }

    static void reportShortcutUsed(Context context, String shortcutId){
        Objects.requireNonNull(context.getSystemService(ShortcutManager.class)).reportShortcutUsed(shortcutId);
    }
}
