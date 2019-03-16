package com.android.kevin.hospitaldatabase.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.android.kevin.hospitaldatabase.data.HospitalContract.PatientsEntry;
import static com.android.kevin.hospitaldatabase.data.HospitalContract.DoctorsEntry;
import static com.android.kevin.hospitaldatabase.data.HospitalContract.NursesEntry;
import static com.android.kevin.hospitaldatabase.data.HospitalContract.WardEntry;

/**
 * Created by Kevin Kimaru Chege on 5/5/2017.
 */

public class HospitalDbHelper extends SQLiteOpenHelper {

    private static  final String DATABASE_NAME = "hospital.db";

    private static final int DATABASE_VERSION = 1;

    public HospitalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PATIENTS_TABLE = "CREATE TABLE " + PatientsEntry.PATIENTS_TABLE_NAME + "("
                + PatientsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PatientsEntry.PATIENT_NAME + " TEXT NOT NULL, "
                + PatientsEntry.PATIENT_GENDER + " INTEGER NOT NULL, "
                + PatientsEntry.PATIENT_AGE_CATEGORY + " INTEGER NOT NULL, "
                + PatientsEntry.PATIENT_DOB + " INTEGER, "
                + PatientsEntry.PATIENT_ILLNESS + " TEXT, "
                + PatientsEntry.ADMISSION_DATE + " INTEGER NOT NULL, "
                + PatientsEntry.DOCTOR_ALLOCATED + " TEXT, "
                + PatientsEntry.WARD_ALLOCATED + " TEXT);";
        db.execSQL(SQL_CREATE_PATIENTS_TABLE);


        String SQL_CREATE_NURSES_TABLE = "CREATE TABLE " + NursesEntry.NURSES_TABLE_NAME + "("
                + NursesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NursesEntry.NURSE_NAME + " TEXT NOT NULL, "
                + NursesEntry.NURSE_GENDER + " INTEGER NOT NULL, "
                + NursesEntry.NURSE_DOB + " INTEGER NOT NULL, "
                + NursesEntry.NURSE_ID_NO + " INTEGER NOT NULL, "
                + NursesEntry.NURSE_START_DATE + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_NURSES_TABLE);

        String SQL_CREATE_DOCTORS_TABLE = "CREATE TABLE " + DoctorsEntry.DOCTORS_TABLE_NAME + "("
                + DoctorsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DoctorsEntry.DOCTOR_NAME + " TEXT NOT NULL, "
                + DoctorsEntry.DOCTOR_GENDER + " TEXT NOT NULL, "
                + DoctorsEntry.DOCTOR_SPECIALIZATION + " TEXT NOT NULL, "
                + DoctorsEntry.DOCTOR_DOB + " INTEGER NOT NULL, "
                + DoctorsEntry.DOCTOR_ID_NO + " INTEGER NOT NULL, "
                + DoctorsEntry.DOCTOR_START_DATE + " INTEGER);";
        db.execSQL(SQL_CREATE_DOCTORS_TABLE);

        String SQL_CREATE_WARDS_TABLE = "CREATE TABLE " + WardEntry.WARD_TABLE_NAME + "("
                + WardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WardEntry.WARD_NAME + " TEXT NOT NULL, "
                + WardEntry.WARD_GENDER + " TEXT NOT NULL, "
                + WardEntry.WARD_AGE_CATEGORY + " INTEGER NOT NULL, "
                + WardEntry.WARD_ILLNESS + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_WARDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
