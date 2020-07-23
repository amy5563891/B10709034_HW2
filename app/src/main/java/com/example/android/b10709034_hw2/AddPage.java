package com.example.android.b10709034_hw2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.b10709034_hw2.data.WaitlistContract;
import com.example.android.b10709034_hw2.data.WaitlistDbHelper;

public class AddPage extends AppCompatActivity {

    private int ab;
    ab = 432;
    
    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;
    private GuestListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpage);
        mNewGuestNameEditText = (EditText) this.findViewById(R.id.person_name_edit_text);
        mNewPartySizeEditText = (EditText) this.findViewById(R.id.party_count_edit_text);
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);
        mDb = dbHelper.getWritableDatabase();


    }
    public void cancel(View view){
        Intent intent = new Intent();
        intent.setClass(AddPage.this , MainActivity.class);
        startActivity(intent);
    }
    public void add(View view) {
        if (mNewGuestNameEditText.getText().length() == 0 || mNewPartySizeEditText.getText().length() == 0) {
            Toast.makeText(this, "輸入錯誤", Toast.LENGTH_LONG).show();
        }else {
            int partySize = 1;
            try {
                partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
            } catch (NumberFormatException ex) {
                Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
            }

            addNewGuest(mNewGuestNameEditText.getText().toString(), partySize);

            mNewPartySizeEditText.clearFocus();
            mNewGuestNameEditText.getText().clear();
            mNewPartySizeEditText.getText().clear();

            Intent intent = new Intent();
            intent.setClass(AddPage.this, MainActivity.class);
            startActivity(intent);
        }
    }
    private Cursor getAllGuests() {
        return mDb.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }
    private long addNewGuest(String name, int partySize) {
        ContentValues cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, partySize);
        return mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, cv);
    }
}
