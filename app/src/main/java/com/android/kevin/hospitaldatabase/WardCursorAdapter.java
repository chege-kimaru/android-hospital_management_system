package com.android.kevin.hospitaldatabase;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.android.kevin.hospitaldatabase.data.HospitalContract;

/**
 * Created by Kevin Kimaru Chege on 4/30/2017.
 */

public class WardCursorAdapter {

    public WardCursorAdapter() {}


    public static final class SelectedWardCursor extends CursorAdapter {
        public SelectedWardCursor(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView nameTextView = (TextView) view.findViewById(R.id.name);
            TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

            int nameColumnIndex = cursor.getColumnIndex(HospitalContract.WardEntry.WARD_NAME);
            int illnessColumnIndex = cursor.getColumnIndex(HospitalContract.WardEntry.WARD_ILLNESS);

            String wardName = cursor.getString(nameColumnIndex);
            String wardIllness = cursor.getString(illnessColumnIndex);

            nameTextView.setText(wardName);
            summaryTextView.setText(wardIllness);



        }
    }


    public static final class AllWardsCursor extends CursorAdapter {
        public AllWardsCursor(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView nameTextView = (TextView) view.findViewById(R.id.name);
            TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

            int nameColumnIndex = cursor.getColumnIndex(HospitalContract.WardEntry.WARD_NAME);
            int specializationColumnIndex = cursor.getColumnIndex(HospitalContract.WardEntry.WARD_ILLNESS);

            String wardName = cursor.getString(nameColumnIndex);
            String wardIllness = cursor.getString(specializationColumnIndex);

            nameTextView.setText(wardName);
            summaryTextView.setText(wardIllness);



        }
    }

}
