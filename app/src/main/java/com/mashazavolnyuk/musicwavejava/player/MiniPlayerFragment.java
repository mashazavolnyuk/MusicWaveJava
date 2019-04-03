package com.mashazavolnyuk.musicwavejava.player;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.MusicServiceFragment;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.helper.MusicPlayerRemote;
import com.mashazavolnyuk.musicwavejava.helper.MusicProgressViewUpdateHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MiniPlayerFragment extends MusicServiceFragment implements MusicProgressViewUpdateHelper.Callback  {

    private Unbinder unbinder;

    @BindView(R.id.mini_player_title)
    TextView miniPlayerTitle;
    @BindView(R.id.mini_player_play_pause_button)
    ImageView miniPlayerPlayPauseButton;
    @BindView(R.id.progress_bar)
    MaterialProgressBar progressBar;

    private MusicProgressViewUpdateHelper progressViewUpdateHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mini_player, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        progressViewUpdateHelper.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        progressViewUpdateHelper.stop();
    }

    @Override
    public void onPlayingMetaChanged() {
        updateSongTitle();
    }

    private void updateSongTitle() {
        miniPlayerTitle.setText(MusicPlayerRemote.getCurrentSong().getTitle());
    }


    @Override
    public void onUpdateProgressViews(int progress, int total) {
        if(progressBar!=null){
            progressBar.setMax(total);
            progressBar.setProgress(progress);
        }
    }
}
