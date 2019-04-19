package com.mashazavolnyuk.musicwavejava.musicService.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.musicService.MusicService;
import com.mashazavolnyuk.musicwavejava.util.ImageUtil;


public class PlayingNotificationImpl24 extends PlayingNotification {

    @Override
    public Notification update() {
        final Song song = service.getCurrentSong();
        final boolean isPlaying = service.isPlaying();
        final RemoteViews notificationLayout = new RemoteViews(service.getPackageName(), R.layout.notification);
        linkButtons(notificationLayout);
        updateContent(song, notificationLayout);
        Notification notification
         = new NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_circle)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContent(notificationLayout)
                .setOngoing(isPlaying)
                .build();
        updateNotifyModeAndPostNotification(notification);
        return notification;
    }

    private void updateContent(Song song, RemoteViews notificationLayout) {
        if (song != null) {
            String artistName = song.getArtistName();
            String title = song.getTitle();
            if (artistName != null) {
                notificationLayout.setTextViewText(R.id.title, song.getArtistName());
            }
            if (title != null) {
                notificationLayout.setTextViewText(R.id.text, song.getTitle());
            }
        }
        Bitmap play_pause = ImageUtil.createBitmap(ImageUtil.getDrawable(service, service.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play), 1.5f);
        notificationLayout.setImageViewBitmap(R.id.action_play_pause,  play_pause);
    }

    private void linkButtons(final RemoteViews notificationLayout) {
        PendingIntent pendingIntent;
        final ComponentName serviceName = new ComponentName(service, MusicService.class);
        // Previous track
        pendingIntent = buildPendingIntent(service, MusicService.ACTION_PREVIOUS, serviceName);
        notificationLayout.setOnClickPendingIntent(R.id.action_prev, pendingIntent);

        // Play and pause
        pendingIntent = buildPendingIntent(service, MusicService.ACTION_PLAY_PAUSE, serviceName);
        notificationLayout.setOnClickPendingIntent(R.id.action_play_pause, pendingIntent);

        // Next track
        pendingIntent = buildPendingIntent(service, MusicService.ACTION_NEXT, serviceName);
        notificationLayout.setOnClickPendingIntent(R.id.action_next, pendingIntent);
    }

    private PendingIntent buildPendingIntent(Context context, final String action, final ComponentName serviceName) {
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);
        return PendingIntent.getService(context, 0, intent, 0);
    }


}
