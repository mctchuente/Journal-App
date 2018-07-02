package com.andela.alcwithgoogle.myjournalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.andela.alcwithgoogle.myjournalapp.model.JournalEntry;

import java.util.Date;

public class AddDiaryActivity extends AppCompatActivity {
    public static final String EXTRA_DIARY_ID = "extraDiaryId";
    public static final String INSTANCE_DIARY_ID = "instanceDiaryId";
    private static final int DEFAULT_DIARY_ID = -1;
    private static final String TAG = AddDiaryActivity.class.getSimpleName();

    EditText mEditText,mEditTextDescription;
    Button mButton;

    private int mDiaryId = DEFAULT_DIARY_ID;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_DIARY_ID)) {
            mDiaryId = savedInstanceState.getInt(INSTANCE_DIARY_ID, DEFAULT_DIARY_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_DIARY_ID)) {
            mButton.setText(R.string.update_button);
            if (mDiaryId == DEFAULT_DIARY_ID) {
                mDiaryId = intent.getIntExtra(EXTRA_DIARY_ID, DEFAULT_DIARY_ID);

                AddDiaryViewModelFactory factory = new AddDiaryViewModelFactory(mDb, mDiaryId);
                final AddDiaryViewModel viewModel = ViewModelProviders.of(this, factory).get(AddDiaryViewModel.class);

                viewModel.getDiary().observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry diaryEntry) {
                        viewModel.getDiary().removeObserver(this);
                        populateUI(diaryEntry);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_DIARY_ID, mDiaryId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditText = findViewById(R.id.editTextDiaryTitle);
        mEditTextDescription = findViewById(R.id.editTextDiaryDescription);

        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param diary the JournalEntry to populate the UI
     */
    private void populateUI(JournalEntry diary) {
        if (diary == null) {
            return;
        }

        mEditText.setText(diary.getTitle());
        mEditTextDescription.setText(diary.getDescription());
    }

    public void onSaveButtonClicked() {
        String title = mEditText.getText().toString();
        String description = mEditTextDescription.getText().toString();
        Date date = new Date();

        final JournalEntry diaryEntry = new JournalEntry(title, description, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mDiaryId == DEFAULT_DIARY_ID) {
                    mDb.journalDao().insertTask(diaryEntry);
                } else {
                    diaryEntry.setId(mDiaryId);
                    mDb.journalDao().updateTask(diaryEntry);
                }
                finish();
            }
        });
    }
}
