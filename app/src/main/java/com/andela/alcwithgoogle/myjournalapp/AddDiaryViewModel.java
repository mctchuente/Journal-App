package com.andela.alcwithgoogle.myjournalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.andela.alcwithgoogle.myjournalapp.model.JournalEntry;

public class AddDiaryViewModel extends ViewModel {
    private LiveData<JournalEntry> diary;

    public AddDiaryViewModel(AppDatabase database, int diaryId) {
        diary = database.journalDao().loadDiaryById(diaryId);
    }

    public LiveData<JournalEntry> getDiary() {
        return diary;
    }
}
