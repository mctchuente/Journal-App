package com.andela.alcwithgoogle.myjournalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.andela.alcwithgoogle.myjournalapp.model.JournalEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<JournalEntry>> diaries;

    public MainViewModel(Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        diaries = database.journalDao().loadAllDiaries();
    }

    public LiveData<List<JournalEntry>> getDiaries() {
        return diaries;
    }
}
