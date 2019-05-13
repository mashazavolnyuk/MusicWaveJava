package com.mashazavolnyuk.musicwavejava.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.mashazavolnyuk.musicwavejava.MainActivity;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.musicService.MusicService;
import com.mashazavolnyuk.musicwavejava.util.ImageUtil;
import com.mashazavolnyuk.musicwavejava.util.MusicUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Arrays;

public class MusicWidget extends AppWidgetProvider {
    final String LOG_TAG = "myLogs";
    boolean isPlay;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        Song song = intent.getParcelableExtra(MusicService.CURRENT_SONG);
        isPlay = intent.getBooleanExtra(MusicService.ACTION_PLAY_PAUSE, false);
        Uri uri;
        if (song != null) {
            uri = MusicUtil.getMediaStoreAlbumCoverUri(song.albumId);
            Picasso.get().load(uri)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            update(context, remoteViews, song, bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            update(context, remoteViews, song, null);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        } else {
            update(context, remoteViews, null, null);
        }
    }

    private void update(Context context, RemoteViews remoteViews, Song song, Bitmap cover) {
        if (song != null) {
            remoteViews.setTextViewText(R.id.artistName, song.getArtistName());
            remoteViews.setTextViewText(R.id.songTitle, song.getTitle());
        } else {
            remoteViews.setTextViewText(R.id.artistName, "Unknown");
            remoteViews.setTextViewText(R.id.songTitle, "Unknown");
        }
        if (cover == null) {
            Bitmap bitmap = ImageUtil.getBitmap(context, R.drawable.ic_music_note);
            remoteViews.setImageViewBitmap(R.id.cover, bitmap);
        } else {
            remoteViews.setImageViewBitmap(R.id.cover, cover);
        }
        int playPauseRes = isPlay ? R.drawable.ic_pause : R.drawable.ic_play;
        remoteViews.setImageViewResource(R.id.playPause, playPauseRes);
        linkButtons(context, remoteViews);
        ComponentName componentName = new ComponentName(context, MusicWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);
            linkButtons(context, remoteViews);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    private void linkButtons(final Context context, final RemoteViews views) {
        Intent action;
        PendingIntent pendingIntent;
        final ComponentName serviceName = new ComponentName(context, MusicService.class);
        // Home
        action = new Intent(context, MainActivity.class);
        action.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(context, 0, action, 0);
        views.setOnClickPendingIntent(R.id.image, pendingIntent);
        views.setOnClickPendingIntent(R.id.media_titles, pendingIntent);
        // Previous track
        pendingIntent = buildPendingIntent(context, MusicService.ACTION_PREVIOUS, serviceName);
        views.setOnClickPendingIntent(R.id.prev, pendingIntent);

        // Play and pause
        pendingIntent = buildPendingIntent(context, MusicService.ACTION_PLAY_PAUSE, serviceName);
        views.setOnClickPendingIntent(R.id.playPause, pendingIntent);

        // Next track
        pendingIntent = buildPendingIntent(context, MusicService.ACTION_NEXT, serviceName);
        views.setOnClickPendingIntent(R.id.next, pendingIntent);
    }

    protected PendingIntent buildPendingIntent(Context context, final String action, final ComponentName serviceName) {
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return PendingIntent.getForegroundService(context, 0, intent, 0);
        } else {
            return PendingIntent.getService(context, 0, intent, 0);
        }
    }
}
