package com.android.kevin.hospitaldatabase;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class AllWardDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView mName, mGender, mAgeCategory, mDisease;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_ward_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = (TextView) findViewById(R.id.wardName);
        mGender = (TextView) findViewById(R.id.wardGender);
        mAgeCategory = (TextView) findViewById(R.id.wardAgeCategory);
        mDisease = (TextView) findViewById(R.id.wardDisease);

        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        if (currentUri != null) {
            setTitle("Ward Details");

            String selection = HospitalContract.WardEntry._ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

            String[] projection = {
                    HospitalContract.WardEntry._ID,
                    HospitalContract.WardEntry.WARD_NAME,
                    HospitalContract.WardEntry.WARD_ILLNESS,
                    HospitalContract.WardEntry.WARD_GENDER,
                    HospitalContract.WardEntry.WARD_AGE_CATEGORY,
            };

            Cursor cursor = getContentResolver().query(
                    HospitalContract.WardEntry.CONTENT_URI,  //The content uri of the words table
                    projection,  //The columns to return for each row
                    selection,   //selection criteria
                    selectionArgs,  //selection criteria
                    null);  //The sort order for the returned rows

            try {

                while (cursor.moveToNext()) {
                    String currentName = cursor.getString(cursor.getColumnIndex(HospitalContract.WardEntry.WARD_NAME));
                    int current_gender = cursor.getInt(cursor.getColumnIndex(HospitalContract.WardEntry.WARD_GENDER));
                    String current_illness = cursor.getString(cursor.getColumnIndex(HospitalContract.WardEntry.WARD_ILLNESS));
                    int current_age_category = cursor.getInt(cursor.getColumnIndex(HospitalContract.WardEntry.WARD_AGE_CATEGORY));

                    mName.setText(currentName);
                    switch (current_gender) {
                        case 0:
                            mGender.setText("male");
                            break;
                        case 1:
                            mGender.setText("female");
                            break;
                        default:
                            mGender.setText("");
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
                            mAgeCategory.setText("");
                    }
                    mDisease.setText(current_illness);
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

        String selection = HospitalContract.WardEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

        getContentResolver().delete(HospitalContract.WardEntry.CONTENT_URI, selection, selectionArgs);

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

            Intent intent2 = new Intent(AllWardDetailsActivity.this, WardsEditorActivity.class);

            //set the uri on the data field of the intent
            intent2.setData(currentUri);
            startActivity(intent2);
        } else if (id == R.id.action_delete) {
            delete();
            finish();

            return true;
        } else if (id == R.id.action_home) {
            Intent intent = new Intent(AllWardDetailsActivity.this, ContentActivity.class);
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
            Intent intent = new Intent(AllWardDetailsActivity.this, WardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_patients) {
            Intent intent = new Intent(AllWardDetailsActivity.this, PatientsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_doctors) {
            Intent intent = new Intent(AllWardDetailsActivity.this, DoctorsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_nurses) {
            Intent intent = new Intent(AllWardDetailsActivity.this, NursesActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
