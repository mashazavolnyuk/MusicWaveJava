package com.mashazavolnyuk.musicwavejava.musicService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mashazavolnyuk.musicwavejava.data.Song;
import com.mashazavolnyuk.musicwavejava.helper.ShuffleHelper;
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

    public static final String INTENT_EXTRA_SHUFFLE_MODE = MUSIC_WAVE_PACKAGE_NAME + ".intentextra.shufflemode";

    public static final String TRACK_ENDED = MUSIC_WAVE_PACKAGE_NAME + ".tack_ended";

    public static final String SAVED_POSITION = "POSITION";
    public static final String SAVED_POSITION_IN_TRACK = "POSITION_IN_TRACK";
    public static final String SAVED_SHUFFLE_MODE = "SHUFFLE_MODE";
    public static final String SAVED_REPEAT_MODE = "REPEAT_MODE";

    public static final int RELEASE_WAKELOCK = 0;
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

    //Used to pause/resume MediaPlayer
    private int resumePosition;
    private List<Song> songListOriginal = new ArrayList<>();
    private List<Song> songList = new ArrayList<>();
    private int position;
    private int shuffleMode;
    private int repeatMode;
    private Song currentSong;
    private final IBinder musicBind = new MusicBinder();
    PlayingNotification playingNotification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public int getAudioSessionId() {
        return playback.getAudioSessionId();
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
            songListOriginal = new ArrayList<>(songList);
            currentSong = songList.get(0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(PlayingNotification.NOTIFICATION_ID, playingNotification.update());
        }
        restoreState();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
                    case ACTION_PLAY:
                        int shuffleMode = intent.getIntExtra(INTENT_EXTRA_SHUFFLE_MODE, SHUFFLE_MODE_NONE);
                        if (shuffleMode != SHUFFLE_MODE_NONE) {
                            if (isPlaying()) {
                                stopMedia();
                            }
                            setShuffleMode(SHUFFLE_MODE_SHUFFLE);
                            playSongAt(position);
                        }
                        break;
                }
            }
        }
        return START_NOT_STICKY;
    }

    private void restoreState() {
        shuffleMode = PreferenceManager.getDefaultSharedPreferences(this).getInt(SAVED_SHUFFLE_MODE, SHUFFLE_MODE_NONE);
        if (shuffleMode == SHUFFLE_MODE_SHUFFLE) {
            ShuffleHelper.makeShuffleList(songList, position);
            position = 0;
            handleAndSendChangeInternal(SHUFFLE_MODE_CHANGED);
        }
        repeatMode = PreferenceManager.getDefaultSharedPreferences(this).getInt(SAVED_REPEAT_MODE, REPEAT_MODE_NONE);

        handleAndSendChangeInternal(REPEAT_MODE_CHANGED);
    }

    private void handleAndSendChangeInternal(@NonNull String what) {
        switch (what) {
            case TRACK_ENDED:
                switch (getRepeatMode()) {
                    case REPEAT_MODE_NONE:
                        if (position >= songList.size() - 1) {
                            pauseMedia();
                            return;
                        } else {
                            playNextSong(true);
                        }
                        break;
                    case REPEAT_MODE_THIS:
                        seek(0);
                        play();
                        break;

                    case REPEAT_MODE_ALL:
                        position++;
                        if (position >= songList.size() - 1) {
                            position = 0;
                        } else {
                            position++;
                        }
                        playMedia(position);
                        break;
                } //switch
            case REPEAT_MODE_CHANGED:
                notifyChange(REPEAT_MODE_CHANGED);
                break;
            case SHUFFLE_MODE_CHANGED:
                notifyChange(SHUFFLE_MODE_CHANGED);
                break;
        }
    }

    private void notifyChange(@NonNull final String what) {
        sendBroadcast(new Intent(what));
    }

    public void updateNotification() {
        if (playingNotification != null && getCurrentSong().id != -1) {
            playingNotification.update();
        }
    }

    @Override
    public void onTrackEnded() {
        handleAndSendChangeInternal(TRACK_ENDED);
    }

    public void play() {
        playback.play();
        notifyChange(PLAY_STATE_CHANGED);
        updateNotification();
    }

    public boolean isPlaying() {
        return playback != null && playback.isPlaying();
    }

    public void playNextSong(boolean b) {
        position++;
        if (position >= songList.size()) {
            position = songList.size() - 1;
            if (position <= -1) {
                position = 0;
            }
        }
        playMedia(position);
    }

    public void playPreviousSong(boolean b) {
        position--;
        if (position == -1) {
            position++;
        }
        playMedia(position);
    }

    public void playSongAt(int index) {
        playMedia(index);
    }

    public void setSongs(List<Song> songs) {
        songList = songs;
        songListOriginal = new ArrayList<>(songList);
    }

    private void playMedia(int index) {
        Song song = songList.get(index);
        playback.setDataPath(song.data);
        currentSong = song;
        position = index;
        playback.play();
        notifyChange(META_CHANGED);
        updateNotification();
        notifyChange(PLAY_STATE_CHANGED);
    }

    private void playMedia() {
        playback.play();
        updateNotification();
        notifyChange(PLAY_STATE_CHANGED);
    }

    private void stopMedia() {
        playback.stop();
        updateNotification();
        notifyChange(PLAY_STATE_CHANGED);
    }

    public void pauseMedia() {
        if (playback.isPlaying()) {
            playback.pause();
            updateNotification();
            notifyChange(PLAY_STATE_CHANGED);
        }
    }

    public int seek(int millis) {
        synchronized (this) {
            try {
                return playback.seek(millis);
            } catch (Exception e) {
                return -1;
            }
        }
    }

    public void toggleShuffle() {
        if (getShuffleMode() == SHUFFLE_MODE_NONE) {
            setShuffleMode(SHUFFLE_MODE_SHUFFLE);
        } else {
            setShuffleMode(SHUFFLE_MODE_NONE);
        }
    }

    public int getShuffleMode() {
        return shuffleMode;
    }

    public void setShuffleMode(final int shuffleMode) {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putInt(SAVED_SHUFFLE_MODE, shuffleMode)
                .apply();
        switch (shuffleMode) {
            case SHUFFLE_MODE_SHUFFLE:
                this.shuffleMode = shuffleMode;
                ShuffleHelper.makeShuffleList(songList, position);
                position = 0;
                break;
            case SHUFFLE_MODE_NONE:
                this.shuffleMode = shuffleMode;
                int currentSongId = getCurrentSong().id;
                songList = new ArrayList<>(songListOriginal);
                int newPosition = 0;
                for (Song song : songList) {
                    if (song.id == currentSongId) {
                        newPosition = songList.indexOf(song);
                    }
                }
                position = newPosition;
                break;
        }
        handleAndSendChangeInternal(SHUFFLE_MODE_CHANGED);
    }

    public void cycleRepeatMode() {
        switch (getRepeatMode()) {
            case REPEAT_MODE_NONE:
                setRepeatMode(REPEAT_MODE_ALL);
                break;
            case REPEAT_MODE_ALL:
                setRepeatMode(REPEAT_MODE_THIS);
                break;
            default:
                setRepeatMode(REPEAT_MODE_NONE);
                break;
        }
    }

    public void setRepeatMode(final int repeatMode) {
        switch (repeatMode) {
            case REPEAT_MODE_NONE:
            case REPEAT_MODE_ALL:
            case REPEAT_MODE_THIS:
                this.repeatMode = repeatMode;
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(SAVED_REPEAT_MODE, repeatMode)
                        .apply();
                handleAndSendChangeInternal(REPEAT_MODE_CHANGED);
                break;
        }
    }

    public int getRepeatMode() {
        return repeatMode;
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
