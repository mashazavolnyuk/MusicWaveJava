package com.mashazavolnyuk.musicwavejava;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends SlidingMusicPanelActivity {
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate()");
    }

    @Override
    public void makeContent() {
        toSongsList();
    }

    @Override
    protected View createContentView() {
        @SuppressLint("InflateParams")
        View contentView = getLayoutInflater().inflate(R.layout.activity_main_drawer_layout, null);
        ViewGroup drawerContent = contentView.findViewById(R.id.drawer_content_container);
        drawerContent.addView(wrapSlidingMusicPanel(R.layout.activity_main_content));
        return contentView;
    }

    private void toSongsList() {
        fragmentManager = getSupportFragmentManager();
        MainFragmentNavigation fragmentNavigation = new MainFragmentNavigation();
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
        if (fragmentManager==null || fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }
    }

}
