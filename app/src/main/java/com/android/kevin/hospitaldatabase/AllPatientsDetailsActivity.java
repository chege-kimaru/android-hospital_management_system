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

public class AllPatientsDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView mName, mGender, mDisease, mDOB, mWardAllocated, mDoctorAllocated, mStartDate, mStopDate,
            mAgeCategory;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_patients_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = (TextView) findViewById(R.id.patientName);
        mGender = (TextView) findViewById(R.id.patientGender);
        mAgeCategory = (TextView) findViewById(R.id.patientAgeCategory);
        mDisease = (TextView) findViewById(R.id.patientDisease);
        mDOB = (TextView) findViewById(R.id.patientDoB);
        mWardAllocated = (TextView) findViewById(R.id.patientWard);
        mDoctorAllocated = (TextView) findViewById(R.id.patientDoctor);
        mStartDate = (TextView) findViewById(R.id.patientAdmissionDate);


        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        if (currentUri != null) {
            //This is a new pet so change the appbar to say add pet
            setTitle("Patient Details");


            String selection = HospitalContract.PatientsEntry._ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

            String[] projection = {
                    HospitalContract.PatientsEntry._ID,
                    HospitalContract.PatientsEntry.PATIENT_NAME,
                    HospitalContract.PatientsEntry.PATIENT_GENDER,
                    HospitalContract.PatientsEntry.PATIENT_AGE_CATEGORY,
                    HospitalContract.PatientsEntry.WARD_ALLOCATED,
                    HospitalContract.PatientsEntry.DOCTOR_ALLOCATED,
                    HospitalContract.PatientsEntry.PATIENT_DOB,
                    HospitalContract.PatientsEntry.PATIENT_ILLNESS,
                    HospitalContract.PatientsEntry.ADMISSION_DATE

            };

            Cursor cursor = getContentResolver().query(
                    HospitalContract.PatientsEntry.CONTENT_URI,  //The content uri of the words table
                    projection,  //The columns to return for each row
                    selection,   //selection criteria
                    selectionArgs,  //selection criteria
                    null);  //The sort order for the returned rows

            try {

                while (cursor.moveToNext()) {

                    int currentID = cursor.getInt(cursor.getColumnIndex(HospitalContract.PatientsEntry.PATIENT_ID));
                    String currentName = cursor.getString(cursor.getColumnIndex(HospitalContract.PatientsEntry.PATIENT_NAME));
                    String currentDoctorAllocated = cursor.getString(cursor.getColumnIndex(HospitalContract.PatientsEntry.DOCTOR_ALLOCATED));
                    int current_gender = cursor.getInt(cursor.getColumnIndex(HospitalContract.PatientsEntry.PATIENT_GENDER));
                    int current_age_category = cursor.getInt(cursor.getColumnIndex(HospitalContract.PatientsEntry.PATIENT_AGE_CATEGORY));
                    String current_ward = cursor.getString(cursor.getColumnIndex(HospitalContract.PatientsEntry.WARD_ALLOCATED));
                    String current_illness = cursor.getString(cursor.getColumnIndex(HospitalContract.PatientsEntry.PATIENT_ILLNESS));
                    long current_DoB = cursor.getLong(cursor.getColumnIndex(HospitalContract.PatientsEntry.PATIENT_DOB));
                    long current_startDate = cursor.getLong(cursor.getColumnIndex(HospitalContract.PatientsEntry.ADMISSION_DATE));


                    mName.setText(currentName, TextView.BufferType.EDITABLE);
                    mDoctorAllocated.setText(currentDoctorAllocated);
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

                    switch (current_age_category) {
                        case 0:
                            mAgeCategory.setText("child");
                            break;
                        case 1:
                            mAgeCategory.setText("youth");
                            break;
                        case 2:
                            mAgeCategory.setText("adult");
                            break;
                        case 3:
                            mAgeCategory.setText("elderly");
                            break;
                        default:
                            mAgeCategory.setText("***");
                    }
                    mDOB.setText(current_DoB + "");
                    mWardAllocated.setText(current_ward);
                    String dateString = DateFormat.format("MM/dd/yyyy", new Date(current_startDate)).toString();
                    mStartDate.setText(dateString);
                    String dateStr = DateFormat.format("MM/dd/yyyy", new Date(current_DoB)).toString();
                    mDOB.setText(dateStr);
                    mDisease.setText(current_illness + "");
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

        String selection = HospitalContract.PatientsEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

        getContentResolver().delete(HospitalContract.PatientsEntry.CONTENT_URI, selection, selectionArgs);

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

        if (id == R.id.action_edit) {

            Intent intent = getIntent();
            Uri currentUri = intent.getData();

            Intent intent2 = new Intent(AllPatientsDetailsActivity.this, PatientsEditorActivity.class);

            //set the uri on the data field of the intent
            intent2.setData(currentUri);
            startActivity(intent2);

        } else if (id == R.id.action_delete) {
            delete();
            finish();

            return true;
        } else if (id == R.id.action_home) {
            Intent intent = new Intent(AllPatientsDetailsActivity.this, ContentActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wards) {
            Intent intent = new Intent(AllPatientsDetailsActivity.this, WardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_patients) {
            Intent intent = new Intent(AllPatientsDetailsActivity.this, PatientsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_doctors) {
            Intent intent = new Intent(AllPatientsDetailsActivity.this, DoctorsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_nurses) {
            Intent intent = new Intent(AllPatientsDetailsActivity.this, NursesActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
