package com.mashazavolnyuk.musicwavejava.shortcurts;

import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.util.ImageUtil;

@RequiresApi(api = Build.VERSION_CODES.N_MR1)
public class ShuffleAllShortcut extends BaseShortcut {

    ShuffleAllShortcut(Context context) {
        super(context);
    }

    public static String getId() {
        return ID_PREFIX + "shuffle_all";
    }

    public ShortcutInfo getShortcutInfo() {
        return new ShortcutInfo.Builder(context, getId())
                .setShortLabel(context.getString(R.string.app_shortcut_shuffle_all_short))
                .setLongLabel(context.getString(R.string.app_shortcut_shuffle_all_short))
                .setIcon(Icon.createWithBitmap(ImageUtil.getBitmap(context, R.drawable.ic_shuffle)))
                .setIntent(getPlaySongsIntent(AppShortcutLauncherActivity.SHORTCUT_TYPE_SHUFFLE_ALL))
                .build();
    }
}
