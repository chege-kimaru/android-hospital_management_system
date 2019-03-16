package com.android.kevin.hospitaldatabase;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.format.DateFormat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.kevin.hospitaldatabase.data.HospitalContract;

import java.util.Date;
import java.util.Locale;

public class AllDoctorsDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView mName, mGender, mField, mDOB, mIdNo, mStartDate, mStopDate;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_doctors_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = (TextView) findViewById(R.id.doctorName);
        mGender = (TextView) findViewById(R.id.doctorGender);
        mField = (TextView) findViewById(R.id.doctorField);
        mDOB = (TextView) findViewById(R.id.doctorDoB);
        mIdNo = (TextView) findViewById(R.id.doctorIdNo);
        mStartDate = (TextView) findViewById(R.id.doctorReportDate);

        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        if (currentUri != null) {
            //This is a new pet so change the appbar to say add pet
            setTitle("Doctor details");


            String selection = HospitalContract.DoctorsEntry._ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

            String[] projection = {
                    HospitalContract.DoctorsEntry._ID,
                    HospitalContract.DoctorsEntry.DOCTOR_NAME,
                    HospitalContract.DoctorsEntry.DOCTOR_ID_NO,
                    HospitalContract.DoctorsEntry.DOCTOR_GENDER,
                    HospitalContract.DoctorsEntry.DOCTOR_SPECIALIZATION,
                    HospitalContract.DoctorsEntry.DOCTOR_DOB,
                    HospitalContract.DoctorsEntry.DOCTOR_START_DATE

            };

            Cursor cursor = getContentResolver().query(
                    HospitalContract.DoctorsEntry.CONTENT_URI,  //The content uri of the words table
                    projection,  //The columns to return for each row
                    selection,   //selection criteria
                    selectionArgs,  //selection criteria
                    null);  //The sort order for the returned rows

            try {

                while (cursor.moveToNext()) {
                    int currentID = cursor.getInt(cursor.getColumnIndex(HospitalContract.DoctorsEntry.DOCTOR_ID));
                    String currentName = cursor.getString(cursor.getColumnIndex(HospitalContract.DoctorsEntry.DOCTOR_NAME));
                    String currentId_No = cursor.getString(cursor.getColumnIndex(HospitalContract.DoctorsEntry.DOCTOR_ID_NO));
                    int current_gender = cursor.getInt(cursor.getColumnIndex(HospitalContract.DoctorsEntry.DOCTOR_GENDER));
                    String current_specialization = cursor.getString(cursor.getColumnIndex(HospitalContract.DoctorsEntry.DOCTOR_SPECIALIZATION));
                    long current_DoB = cursor.getLong(cursor.getColumnIndex(HospitalContract.DoctorsEntry.DOCTOR_DOB));
                    long current_startDate = cursor.getLong(cursor.getColumnIndex(HospitalContract.DoctorsEntry.DOCTOR_START_DATE));


                    mName.setText(currentName);
                    mIdNo.setText(currentId_No);
                    mGender.setText(current_gender + "");
                    switch (current_gender) {
                        case 0:
                            mGender.setText("male");
                            break;
                        case 1:
                            mGender.setText("female");
                            break;
                        default:
                            mGender.setText("***");
                    }
                    mDOB.setText(current_DoB + "");
                    mField.setText(current_specialization);
                    String dateString = DateFormat.format("MM/dd/yyyy", new Date(current_startDate)).toString();
                    mStartDate.setText(dateString);
                    String dateStr = DateFormat.format("MM/dd/yyyy", new Date(current_DoB)).toString();
                    mDOB.setText(dateStr);
                }
            } finally {
                cursor.close();
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void delete() {
        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        String selection = HospitalContract.DoctorsEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

        getContentResolver().delete(HospitalContract.DoctorsEntry.CONTENT_URI, selection, selectionArgs);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_doctors_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {

            Intent intent = getIntent();
            Uri currentUri = intent.getData();

            Intent intent2 = new Intent(AllDoctorsDetailsActivity.this, DoctorsEditorActivity.class);

            //set the uri on the data field of the intent
            intent2.setData(currentUri);
            startActivity(intent2);
            return true;
        } else if (id == R.id.action_delete) {
            delete();
            finish();

            return true;
        } else if (id == R.id.action_home) {
            Intent intent = new Intent(AllDoctorsDetailsActivity.this, ContentActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wards) {
            Intent intent = new Intent(AllDoctorsDetailsActivity.this, WardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_patients) {
            Intent intent = new Intent(AllDoctorsDetailsActivity.this, PatientsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_doctors) {
            Intent intent = new Intent(AllDoctorsDetailsActivity.this, DoctorsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_nurses) {
            Intent intent = new Intent(AllDoctorsDetailsActivity.this, NursesActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
