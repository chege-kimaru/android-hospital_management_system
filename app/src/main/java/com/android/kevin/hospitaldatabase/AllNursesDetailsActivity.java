package com.android.kevin.hospitaldatabase;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import com.android.kevin.hospitaldatabase.data.HospitalContract;

import java.util.Date;

public class AllNursesDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView mName, mGender, mDOB, mIdNo, mStartDate, mStopDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_nurses_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = (TextView) findViewById(R.id.nurseName);
        mGender = (TextView) findViewById(R.id.nurseGender);
        mDOB = (TextView) findViewById(R.id.nurseDoB);
        mIdNo = (TextView) findViewById(R.id.nurseIdNo);
        mStartDate = (TextView) findViewById(R.id.nurseReportDate);


        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        //If the intent does not contain a pet content uri , then we know we are creating a new pet
        if (currentUri != null) {
            //This is a new pet so change the appbar to say add pet
            setTitle("Nurse details");


            String selection = HospitalContract.NursesEntry._ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

            String[] projection = {
                    HospitalContract.NursesEntry._ID,
                    HospitalContract.NursesEntry.NURSE_NAME,
                    HospitalContract.NursesEntry.NURSE_ID_NO,
                    HospitalContract.NursesEntry.NURSE_GENDER,
                    HospitalContract.NursesEntry.NURSE_DOB,
                    HospitalContract.NursesEntry.NURSE_START_DATE

            };

            Cursor cursor = getContentResolver().query(
                    HospitalContract.NursesEntry.CONTENT_URI,  //The content uri of the words table
                    projection,  //The columns to return for each row
                    selection,   //selection criteria
                    selectionArgs,  //selection criteria
                    null);  //The sort order for the returned rows

            try {

                while (cursor.moveToNext()) {
                    int currentID = cursor.getInt(cursor.getColumnIndex(HospitalContract.NursesEntry.NURSES_ID));
                    String currentName = cursor.getString(cursor.getColumnIndex(HospitalContract.NursesEntry.NURSE_NAME));
                    String currentId_No = cursor.getString(cursor.getColumnIndex(HospitalContract.NursesEntry.NURSE_ID_NO));
                    int current_gender = cursor.getInt(cursor.getColumnIndex(HospitalContract.NursesEntry.NURSE_GENDER));
                    long current_DoB = cursor.getLong(cursor.getColumnIndex(HospitalContract.NursesEntry.NURSE_DOB));
                    long current_startDate = cursor.getLong(cursor.getColumnIndex(HospitalContract.NursesEntry.NURSE_START_DATE));

                    mName.setText(currentName);
                    mIdNo.setText(currentId_No);
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

        String selection = HospitalContract.NursesEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

        getContentResolver().delete(HospitalContract.NursesEntry.CONTENT_URI, selection, selectionArgs);

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

            Intent intent2 = new Intent(AllNursesDetailsActivity.this, NursesEditorActivity.class);

            //set the uri on the data field of the intent
            intent2.setData(currentUri);
            startActivity(intent2);

            return true;
        } else if (id == R.id.action_delete) {
            delete();
            finish();

            return true;
        } else if (id == R.id.action_home) {
            Intent intent = new Intent(AllNursesDetailsActivity.this, ContentActivity.class);
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
            Intent intent = new Intent(AllNursesDetailsActivity.this, WardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_patients) {
            Intent intent = new Intent(AllNursesDetailsActivity.this, PatientsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_doctors) {
            Intent intent = new Intent(AllNursesDetailsActivity.this, DoctorsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_nurses) {
            Intent intent = new Intent(AllNursesDetailsActivity.this, NursesActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
