package com.andela.alcwithgoogle.myjournalapp;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andela.alcwithgoogle.myjournalapp.model.JournalEntry;

public class DetailDiaryActivity extends AppCompatActivity {
    public static final String EXTRA_DIARY_ID = "extraDiaryId";
    public static final String INSTANCE_DIARY_ID = "instanceDiaryId";
    private static final int DEFAULT_DIARY_ID = -1;
    private static final String TAG = DetailDiaryActivity.class.getSimpleName();

    TextView mTextViewTitle, mTextViewDescription;
    Button mButton;

    private int mDiaryId = DEFAULT_DIARY_ID;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_diary);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_DIARY_ID)) {
            mDiaryId = savedInstanceState.getInt(INSTANCE_DIARY_ID, DEFAULT_DIARY_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_DIARY_ID)) {
            mDiaryId = intent.getIntExtra(EXTRA_DIARY_ID, DEFAULT_DIARY_ID);
            if (mDiaryId != DEFAULT_DIARY_ID) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final JournalEntry diary = mDb.journalDao().findDiaryById(mDiaryId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(diary);
                            }
                        });
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

    private void initViews() {
        mTextViewTitle = findViewById(R.id.textViewDiaryTitle);
        mTextViewDescription = findViewById(R.id.textViewDiaryDescription);

        mButton = findViewById(R.id.editButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    private void populateUI(JournalEntry diary) {
        if (diary == null) {
            return;
        }

        mTextViewTitle.setText(diary.getTitle());
        mTextViewDescription.setText(diary.getDescription());
    }

    public void onSaveButtonClicked() {
        Intent intent = new Intent(DetailDiaryActivity.this, AddDiaryActivity.class);
        intent.putExtra(AddDiaryActivity.EXTRA_DIARY_ID, mDiaryId);
        startActivity(intent);
    }
}
