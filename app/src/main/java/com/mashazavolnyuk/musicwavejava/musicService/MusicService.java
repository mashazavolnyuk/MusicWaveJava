package com.mashazavolnyuk.musicwavejava.musicService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.mashazavolnyuk.musicwavejava.BuildConfig;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.loader.SongLoader;
import com.mashazavolnyuk.musicwavejava.musicService.notification.PlayingNotification;
import com.mashazavolnyuk.musicwavejava.musicService.notification.PlayingNotificationImpl24;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MusicService extends Service implements Playback.PlaybackCallbacks {

    private String TAG = "MusicService";

    public static final String MUSIC_WAVE_PACKAGE_NAME = "com.mashazavolnyuk.musicwavejava";

    public static final String ACTION_PLAY = "com.mashazavolnyuk.musicwavejava.ACTION_PLAY";
    public static final String ACTION_PLAY_PAUSE = "com.mashazavolnyuk.musicwavejava.ACTION_PLAY_PAUSE";
    public static final String ACTION_RESUME = "com.mashazavolnyuk.musicwavejava.ACTION_RESUME";
    public static final String ACTION_PAUSE = "com.mashazavolnyuk.musicwavejava.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.mashazavolnyuk.musicwavejava.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.mashazavolnyuk.musicwavejava.ACTION_NEXT";
    public static final String ACTION_STOP = "com.mashazavolnyuk.musicwavejava.ACTION_STOP";


    // do not change these three strings as it will break support with other apps (e.g. last.fm scrobbling)
    public static final String META_CHANGED = MUSIC_WAVE_PACKAGE_NAME + ".metachanged";
    public static final String QUEUE_CHANGED = MUSIC_WAVE_PACKAGE_NAME + ".queuechanged";
    public static final String PLAY_STATE_CHANGED = MUSIC_WAVE_PACKAGE_NAME + ".playstatechanged";

    public static final String REPEAT_MODE_CHANGED = MUSIC_WAVE_PACKAGE_NAME + ".repeatmodechanged";
    public static final String SHUFFLE_MODE_CHANGED = MUSIC_WAVE_PACKAGE_NAME + ".shufflemodechanged";
    public static final String MEDIA_STORE_CHANGED = MUSIC_WAVE_PACKAGE_NAME + ".mediastorechanged";

    public static final String SAVED_POSITION = "POSITION";
    public static final String SAVED_POSITION_IN_TRACK = "POSITION_IN_TRACK";
    public static final String SAVED_SHUFFLE_MODE = "SHUFFLE_MODE";
    public static final String SAVED_REPEAT_MODE = "REPEAT_MODE";

    public static final int RELEASE_WAKELOCK = 0;
    public static final int TRACK_ENDED = 1;
    public static final int TRACK_WENT_TO_NEXT = 2;
    public static final int PLAY_SONG = 3;
    public static final int PREPARE_NEXT = 4;
    public static final int SET_POSITION = 5;
    private static final int FOCUS_CHANGE = 6;
    private static final int DUCK = 7;
    private static final int UNDUCK = 8;
    public static final int RESTORE_QUEUES = 9;

    public static final int SHUFFLE_MODE_NONE = 0;
    public static final int SHUFFLE_MODE_SHUFFLE = 1;

    public static final int REPEAT_MODE_NONE = 0;
    public static final int REPEAT_MODE_ALL = 1;
    public static final int REPEAT_MODE_THIS = 2;

    private Playback playback;

    //MediaSession
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    private Notification notification;
    private String channelId = "musicWave";

    //Used to pause/resume MediaPlayer
    private int resumePosition;

    private List<Song> songList = new ArrayList<>();
    private int position;
    private Song currentSong;
    private final IBinder musicBind = new MusicBinder();
    PlayingNotification playingNotification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public class MusicBinder extends Binder {
        @NonNull
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        playback = new Player(this);
        playback.setCallbacks(this);
        playingNotification = new PlayingNotificationImpl24();
        playingNotification.init(this);
        if (songList.size() == 0) {
            songList = SongLoader.getSongList(this);
            Collections.sort(songList, (a, b) -> a.getTitle().compareTo(b.getTitle()));
            currentSong = songList.get(0);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(22345, playingNotification.update());
        }
        if (intent != null) {
            if (intent.getAction() != null) {
                String action = intent.getAction();
                switch (action) {
                    case ACTION_PREVIOUS:
                        playPreviousSong(true);
                        break;
                    case ACTION_NEXT:
                        playNextSong(true);
                        break;
                    case ACTION_PLAY_PAUSE:
                        if (isPlaying()) {
                            pauseMedia();
                        } else {
                            playMedia();
                        }
                        break;
                }
            }
        }
        return START_NOT_STICKY;
    }


    private void notifyChange(@NonNull final String what) {
        sendBroadcast(new Intent(what));
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
//            transportControls.play();
            playMedia();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            // transportControls.pause();
            pauseMedia();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            //  transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            //  transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            //  transportControls.stop();
            stopMedia();
        }
    }

    private void initMediaSession() {
        if (mediaSessionManager != null) return; //mediaSessionManager exists
        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();

                resumeMedia();
//                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onPause() {
                super.onPause();

                pauseMedia();
//                buildNotification(PlaybackStatus.PAUSED);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();

//                skipToNext();
//                updateMetaData();
//                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
//
//                skipToPrevious();
//                updateMetaData();
//                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop() {
                super.onStop();
//                removeNotification();
                //Stop the service
                stopSelf();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }
        });
    }

    @Override
    public void onTrackEnded() {
        playNextSong(true);
    }

    public void play() {
        playback.play();
        notifyChange(PLAY_STATE_CHANGED);
    }

    public boolean isPlaying() {
        return playback != null && playback.isPlaying();
    }

    public void playNextSong(boolean b) {
        position++;
        currentSong = songList.get(position);
        playback.setDataPath(currentSong.data);
        playback.play();
        notifyChange(MusicService.META_CHANGED);
        notifyChange(MusicService.PLAY_STATE_CHANGED);
    }

    public void playPreviousSong(boolean b) {
        position--;
        currentSong = songList.get(position);
        playback.setDataPath(currentSong.data);
        playback.play();
        notifyChange(MusicService.META_CHANGED);
        notifyChange(MusicService.PLAY_STATE_CHANGED);
    }

    public void playSongAt(int index) {
        Song song = songList.get(index);
        playback.setDataPath(song.data);
        currentSong = song;
        position = index;
        playback.play();
        notifyChange(META_CHANGED);
        notifyChange(PLAY_STATE_CHANGED);
    }

    public void setSongs(List<Song> songs) {
        songList = songs;
    }

    private void playMedia() {
        playback.play();
        notifyChange(PLAY_STATE_CHANGED);
    }

    private void stopMedia() {
        playback.stop();
    }

    public void pauseMedia() {
        if (playback.isPlaying()) {
            playback.pause();
            notifyChange(PLAY_STATE_CHANGED);
        }
    }

    public int seek(int millis) {
        synchronized (this) {
            try {
                int newPosition = playback.seek(millis);
                return newPosition;
            } catch (Exception e) {
                return -1;
            }
        }
    }

    private void resumeMedia() {

    }

    public int getSongProgressMillis() {
        return playback.position();
    }

    public int getSongDurationMillis() {
        return playback.duration();
    }

    public Song getCurrentSong() {
        return currentSong;
    }

}
