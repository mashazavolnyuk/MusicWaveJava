package com.mashazavolnyuk.musicwavejava.search;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.mashazavolnyuk.musicwavejava.MusicServiceActivity;
import com.mashazavolnyuk.musicwavejava.R;
import com.mashazavolnyuk.musicwavejava.adpater.SearchAdapter;
import com.mashazavolnyuk.musicwavejava.data.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultsActivity extends MusicServiceActivity implements SearchView.OnQueryTextListener, LifecycleOwner {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(android.R.id.empty)
    TextView empty;

    SearchView searchView;
    private SearchAdapter adapter;
    SearchDataViewModel searchDataViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(this, Collections.emptyList());
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                empty.setVisibility(adapter.getItemCount() < 1 ? View.VISIBLE : View.GONE);
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setOnTouchListener((v, event) -> {
            hideSoftKeyboard(SearchResultsActivity.this);
            return false;
        });
        SearchViewModelFactory searchViewModelFactory = new SearchViewModelFactory(getApplication());
        searchDataViewModel = ViewModelProviders.of(this, searchViewModelFactory).get(SearchDataViewModel.class);

    }

    @Override
    public void makeContent() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchItem.expandActionView();
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                onBackPressed();
                return false;
            }
        });
        searchView.post(() -> searchView.setOnQueryTextListener(this));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        hideSoftKeyboard(this);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s == null || s.isEmpty()) {
            adapter.swapDataSet(new ArrayList<Song>());
        } else {
            search(s);
        }

        return false;
    }

    public static void hideSoftKeyboard(@Nullable Activity activity) {
        if (activity != null) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }

    private void search(String query) {
        searchDataViewModel.loadSongs(query).observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(@Nullable List<Song> songs) {
                adapter.swapDataSet(songs);
            }
        });
    }
}
