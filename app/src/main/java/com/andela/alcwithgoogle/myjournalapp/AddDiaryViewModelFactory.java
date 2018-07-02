package com.andela.alcwithgoogle.myjournalapp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class AddDiaryViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mDiaryId;

    public AddDiaryViewModelFactory(AppDatabase database, int diaryId) {
        mDb = database;
        mDiaryId = diaryId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddDiaryViewModel(mDb, mDiaryId);
    }
}
