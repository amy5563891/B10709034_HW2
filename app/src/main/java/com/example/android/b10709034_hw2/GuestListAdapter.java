package com.example.android.b10709034_hw2;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.b10709034_hw2.data.WaitlistContract;


public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestViewHolder> implements SharedPreferences.OnSharedPreferenceChangeListener{

    private Cursor mCursor;
    private Context mContext;
    public GuestViewHolder holder;
    TextView nameTextView;
    TextView partySizeTextView;
    Drawable mColor;
    public GuestListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guest_list_item, parent, false);

        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        this.holder=holder;
        if (!mCursor.moveToPosition(position))
            return;
        String name = mCursor.getString(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME));
        int partySize = mCursor.getInt(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE));
        long id = mCursor.getLong(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry._ID));

        nameTextView.setText(name);
        partySizeTextView.setText(String.valueOf(partySize));
        holder.itemView.setTag(id); //在recyclerview裡幫個別item加上tag，當swipe物件，從Viewholder找出這個物件的View

        setupSharedPreferences();
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        setColor(sharedPreferences.getString(mContext.getResources().getString(R.string.pref_color_key),mContext.getResources().getString(R.string.pref_color_key)));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
    public void setColor(String color){

        if (color.equals(mContext.getString(R.string.pref_color_red_value))) {
            mColor = ContextCompat.getDrawable(mContext, R.drawable.redcircle);
        } else if (color.equals(mContext.getString(R.string.pref_color_blue_value))) {
            mColor = ContextCompat.getDrawable(mContext, R.drawable.bluecircle);
        } else {
            mColor = ContextCompat.getDrawable(mContext, R.drawable.greencircle);
        }
        partySizeTextView.setBackground(mColor);
    }
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(mContext.getResources().getString(R.string.pref_color_key))){
            setColor(sharedPreferences.getString(key,mContext.getResources().getString(R.string.pref_color_red_value)));//改背景
        }
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    class GuestViewHolder extends RecyclerView.ViewHolder {
        public GuestViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);

        }

    }
}