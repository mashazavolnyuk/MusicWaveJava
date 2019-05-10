package com.mashazavolnyuk.musicwavejava.search;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class SearchViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    public SearchViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchDataViewModel.class)) {
            return (T) new SearchDataViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
