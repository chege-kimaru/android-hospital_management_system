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

public class NursesCursorAdapter {

    public NursesCursorAdapter() {}


    public static final class SelectedNursesCursor extends CursorAdapter {
        public SelectedNursesCursor(Context context, Cursor c) {
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

            int nameColumnIndex = cursor.getColumnIndex(HospitalContract.NursesEntry.NURSE_NAME);
            int genderColumnIbdex = cursor.getColumnIndex(HospitalContract.NursesEntry.NURSE_GENDER);

            String nurseName = cursor.getString(nameColumnIndex);
            int nurseGender = cursor.getInt(genderColumnIbdex);

            switch (nurseGender) {
                case 0:
                    summaryTextView.setText("male");
                    break;
                case 1:
                    summaryTextView.setText("female");
                    break;
                default:
                    summaryTextView.setText("***");
            }
            nameTextView.setText(nurseName);




        }
    }


    public static final class AllNursesCursor extends CursorAdapter {
        public AllNursesCursor(Context context, Cursor c) {
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

            int nameColumnIndex = cursor.getColumnIndex(HospitalContract.NursesEntry.NURSE_NAME);
            int genderColumnIndex = cursor.getColumnIndex(HospitalContract.NursesEntry.NURSE_GENDER);

            String nurseNamee = cursor.getString(nameColumnIndex);
            String nurseGender = cursor.getString(genderColumnIndex);

            nameTextView.setText(nurseNamee);
            summaryTextView.setText(nurseGender);



        }
    }

}
