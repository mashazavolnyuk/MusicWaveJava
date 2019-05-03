package com.mashazavolnyuk.musicwavejava.albums;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.SlidingMusicPanelActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class AlbumDetail extends SlidingMusicPanelActivity {

    public static final String EXTRA_ALBUM_ID = "extra_album_id";
    private FragmentManager fragmentManager;

    @Override
    protected View createContentView() {
        @SuppressLint("InflateParams")
        View contentView = getLayoutInflater().inflate(R.layout.activity_main_drawer_layout, null);
        ViewGroup drawerContent = contentView.findViewById(R.id.drawer_content_container);
        drawerContent.addView(wrapSlidingMusicPanel(R.layout.activity_main_content));
        return contentView;
    }

    @Override
    public void makeContent() {
        showDetailAlbum();
    }

    private void showDetailAlbum(){
        fragmentManager = getSupportFragmentManager();
        FragmentDetailAlbum fragmentNavigation = new FragmentDetailAlbum();
        Bundle extras = getIntent().getExtras();
        fragmentNavigation.setArguments(extras);
        fragmentManager.beginTransaction().replace(R.id.content, fragmentNavigation).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (slidingUpPanelLayout != null) {
            if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            else {
                backStackFragments();
            }
        }else {
            backStackFragments();
        }
    }

    private void backStackFragments(){
        if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }
    }

}
