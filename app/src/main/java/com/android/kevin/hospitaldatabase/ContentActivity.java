package com.android.kevin.hospitaldatabase;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.Button;
import android.widget.TextView;

import com.android.kevin.hospitaldatabase.data.HospitalContract;

public class ContentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btn_ward, btn_nurses, btn_doctors, btn_patients;
    TextView tv_ward, tv_nurse, tv_doc, tv_patient;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content3);

        Cursor cursorW = getContentResolver().query(
                HospitalContract.WardEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        int wardCursor = cursorW.getCount();

        Cursor cursorP = getContentResolver().query(
                HospitalContract.PatientsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        int patientCursor = cursorP.getCount();

        Cursor cursorN = getContentResolver().query(
                HospitalContract.NursesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        int nurseCursor = cursorN.getCount();

        Cursor cursorD = getContentResolver().query(
                HospitalContract.DoctorsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        int doctorCursor = cursorD.getCount();

        btn_ward = (Button) findViewById(R.id.wards_btn);
        btn_doctors = (Button) findViewById(R.id.doctors_btn);
        btn_nurses = (Button) findViewById(R.id.nurses_btn);
        btn_patients= (Button) findViewById(R.id.patients_btn);

        tv_doc = (TextView) findViewById(R.id.doctorsText);
        tv_doc.setText(doctorCursor + " doctor(s)");
        tv_nurse = (TextView) findViewById(R.id.nursesText);
        tv_nurse.setText(nurseCursor + " nurse(s)");
        tv_patient = (TextView) findViewById(R.id.patientText);
        tv_patient.setText(patientCursor + " patient(s)");
        tv_ward = (TextView) findViewById(R.id.wardText);
        tv_ward.setText(wardCursor + " ward(s)");

        btn_ward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentActivity.this, WardsActivity.class);
                startActivity(intent);
            }
        });
        btn_doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentActivity.this, DoctorsActivity.class);
                startActivity(intent);
            }
        });
        btn_nurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentActivity.this, NursesActivity.class);
                startActivity(intent);
            }
        });
        btn_patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentActivity.this, PatientsActivity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_patients) {
            Intent intent = new Intent(ContentActivity.this, PatientsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_wards) {
            Intent intent = new Intent(ContentActivity.this, WardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_nurses) {
            Intent intent = new Intent(ContentActivity.this, NursesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_doctors) {
            Intent intent = new Intent(ContentActivity.this, DoctorsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
