package com.andela.alcwithgoogle.myjournalapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.andela.alcwithgoogle.myjournalapp.model.JournalEntry;

import java.util.List;

@Dao
public interface JournalDao {
    @Query("SELECT * FROM journal ORDER BY updated_at DESC")
    LiveData<List<JournalEntry>> loadAllDiaries();

    @Insert
    void insertTask(JournalEntry diaryEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(JournalEntry diaryEntry);

    @Delete
    void deleteTask(JournalEntry diaryEntry);

    @Query("SELECT * FROM journal WHERE id = :id")
    LiveData<JournalEntry> loadDiaryById(int id);

    @Query("SELECT * FROM journal WHERE id = :id")
    JournalEntry findDiaryById(int id);
}
