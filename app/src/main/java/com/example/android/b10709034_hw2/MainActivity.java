package com.example.android.b10709034_hw2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.b10709034_hw2.data.WaitlistContract;
import com.example.android.b10709034_hw2.data.WaitlistDbHelper;


public class MainActivity extends AppCompatActivity {

    private GuestListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private RecyclerView waitlistRecyclerView;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        Cursor cursor = getAllGuests();
        mAdapter = new GuestListAdapter(this, cursor);


        waitlistRecyclerView.setAdapter(mAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final long id = (long) viewHolder.itemView.getTag(); //取得被滑動的item.tag(id)

                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setTitle("Note");
                a.setMessage("check Delete?");

                a.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int num) {
                        removeGuest(id);
                        mAdapter.swapCursor(getAllGuests());
                    }
                });
                a.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int num) {

                    }
                });
                a.show();

                mAdapter.swapCursor(getAllGuests());
            }
        }).attachToRecyclerView(waitlistRecyclerView); //recyclerview上面加上touchhelper
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 取得點選項目的id
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.action_add) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this , AddPage.class);
            startActivity(intent);
        }
        else if (id == R.id.action_setting) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this , SettingPage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
    private boolean removeGuest(long id) {
        return mDb.delete(WaitlistContract.WaitlistEntry.TABLE_NAME,
                WaitlistContract.WaitlistEntry._ID + "=" + id, null) > 0;
    }

}