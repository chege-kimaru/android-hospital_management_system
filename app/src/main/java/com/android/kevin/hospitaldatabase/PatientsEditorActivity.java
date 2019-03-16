package com.android.kevin.hospitaldatabase;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.kevin.hospitaldatabase.data.HospitalContract;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.android.kevin.hospitaldatabase.data.HospitalContract.PatientsEntry;

public class PatientsEditorActivity extends AppCompatActivity {

    Spinner ageCategorySpinner;
    Spinner doctorSpinner;
    String doctorAllocatedStr;
    Spinner wardSpinner;
    String wardAllocatedStr;

    DatePicker dateOfBirth_datePicker;
    DatePicker admissionDate_datePicker;


    EditText mNameEdit;
    TextView mDoBView;
    TextView mAdmissionDateView;
    TextView mDischargeDateView;
    EditText mIllness;
    TextView mWardText, mDoctorAllocatedText, mAdmissionDateText, mDischargeDateText;
    Spinner mGenderSpinner;
    private int mGender = 0;
    private int mAgeCategory = 1;


    int mState;

    Button save_btn, save_changes_btn;

    String[] projectionW;

    Cursor cursorW;
    List<String> wards;

    ArrayAdapter<String> dataAdapterW;

    String[] projectionD;

    Cursor cursorD;
    List<String> doctors;

    ArrayAdapter<String> dataAdapterD;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_editor);

        doctorSpinner = (Spinner) findViewById(R.id.spinner_doctorAllocated);
        wardSpinner = (Spinner) findViewById(R.id.spinner_wardAllocated);
        ageCategorySpinner = (Spinner) findViewById(R.id.spinner_age_category);

        admissionDate_datePicker = (DatePicker) findViewById(R.id.datePickerPAdmD);
        dateOfBirth_datePicker = (DatePicker) findViewById(R.id.datePickerPDob);


        projectionW = new String[]{
                HospitalContract.WardEntry._ID,
                HospitalContract.WardEntry.WARD_NAME,
        };

        cursorW = getContentResolver().query(
                HospitalContract.WardEntry.CONTENT_URI,  //The content uri of the words table
                projectionW,  //The columns to return for each row
                null,   //selection criteria
                null,  //selection criteria
                null);  //The sort order for the returned rows
        wards = new ArrayList<String>();

        dataAdapterW = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, wards);


        projectionD = new String[]{
                HospitalContract.DoctorsEntry._ID,
                HospitalContract.DoctorsEntry.DOCTOR_NAME,
        };

        cursorD = getContentResolver().query(
                HospitalContract.DoctorsEntry.CONTENT_URI,  //The content uri of the words table
                projectionD,  //The columns to return for each row
                null,   //selection criteria
                null,  //selection criteria
                null);  //The sort order for the returned rows
        doctors = new ArrayList<String>();

        dataAdapterD = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, doctors);


        Intent intent = getIntent();
        Uri currentUri = intent.getData();


        mNameEdit = (EditText) findViewById(R.id.edit_name);
        mNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mNameEdit.setError("Field Required");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChangedToEmpty(mNameEdit);
            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmpty();
            }
        });
        mDoBView = (TextView) findViewById(R.id.edit_date_of_birth);
        mAdmissionDateView = (TextView) findViewById(R.id.edit_date_of_report);
        mDischargeDateView = (TextView) findViewById(R.id.edit_retirement_date);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mIllness = (EditText) findViewById(R.id.edit_illness);
        mIllness.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mIllness.setError("Field Required");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChangedToEmpty(mIllness);
            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmpty();
            }
        });

        mDoctorAllocatedText = (TextView) findViewById(R.id.text_id_no);
        mDoctorAllocatedText.setText("Doctor Allocated");
        mAdmissionDateText = (TextView) findViewById(R.id.text_date_of_report);
        mAdmissionDateText.setText("Admission Date");
        mWardText = (TextView) findViewById(R.id.text_specialization);
        mWardText.setText("Ward");


        save_btn = (Button) findViewById(R.id.save_btn);
        save_changes_btn = (Button) findViewById(R.id.save_changes_btn);

        setupGenderSpinner();
        setupDoctorsSpinner();
        setupWardSpinner();
        setupAgeCategorySpinner();


        //If the intent does not contain a pet content uri , then we know we are creating a new pet
        if (currentUri != null) {
            //This is a new pet so change the appbar to say add pet
            setTitle("Edit Patient");
            save_btn.setVisibility(View.GONE);

            save_changes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update();
                    Intent intent = new Intent(PatientsEditorActivity.this, PatientsActivity.class);
                    startActivity(intent);
                    Toast.makeText(PatientsEditorActivity.this, "Saved changes successfully", Toast.LENGTH_SHORT).show();
                }
            });

            mState = 1;


            String selection = PatientsEntry._ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

            String[] projection = {
                    PatientsEntry._ID,
                    PatientsEntry.PATIENT_NAME,
                    PatientsEntry.PATIENT_GENDER,
                    PatientsEntry.PATIENT_AGE_CATEGORY,
                    PatientsEntry.WARD_ALLOCATED,
                    PatientsEntry.DOCTOR_ALLOCATED,
                    PatientsEntry.PATIENT_DOB,
                    PatientsEntry.PATIENT_ILLNESS,
                    PatientsEntry.ADMISSION_DATE,

            };

            Cursor cursor = getContentResolver().query(
                    PatientsEntry.CONTENT_URI,  //The content uri of the words table
                    projection,  //The columns to return for each row
                    selection,   //selection criteria
                    selectionArgs,  //selection criteria
                    null);  //The sort order for the returned rows

            try {

                while (cursor.moveToNext()) {
                    int currentID = cursor.getInt(cursor.getColumnIndex(PatientsEntry.PATIENT_ID));
                    String currentName = cursor.getString(cursor.getColumnIndex(PatientsEntry.PATIENT_NAME));
                    String currentDoctorAllocated = cursor.getString(cursor.getColumnIndex(PatientsEntry.DOCTOR_ALLOCATED));
                    int current_gender = cursor.getInt(cursor.getColumnIndex(PatientsEntry.PATIENT_GENDER));
                    int current_ageCategory = cursor.getInt(cursor.getColumnIndex(PatientsEntry.PATIENT_AGE_CATEGORY));
                    String current_ward = cursor.getString(cursor.getColumnIndex(PatientsEntry.WARD_ALLOCATED));
                    String current_illness = cursor.getString(cursor.getColumnIndex(PatientsEntry.PATIENT_ILLNESS));
                    long current_DoB = cursor.getLong(cursor.getColumnIndex(PatientsEntry.PATIENT_DOB));
                    long current_startDate = cursor.getLong(cursor.getColumnIndex(PatientsEntry.ADMISSION_DATE));

                    mNameEdit.setText(currentName, TextView.BufferType.EDITABLE);
                    int wSpinnerPosition = 0;
                    wSpinnerPosition = dataAdapterW.getPosition(current_ward);
                    wardSpinner.setSelection(wSpinnerPosition);
                    int dSpinnerPosition = 0;
                    dSpinnerPosition = dataAdapterD.getPosition(currentDoctorAllocated);
                    doctorSpinner.setSelection(dSpinnerPosition);
                    mGenderSpinner.setSelection(current_gender);
                    ageCategorySpinner.setSelection(current_ageCategory);
                    mIllness.setText(current_illness);
                    dateOfBirth_datePicker.setVisibility(View.GONE);
                    admissionDate_datePicker.setVisibility(View.GONE);

                }
            } finally {
                cursor.close();
            }
        } else {
            setTitle("Add Patient");
            save_changes_btn.setVisibility(View.GONE);


            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insert();
                    finish();
                    Toast.makeText(PatientsEditorActivity.this, "Saved successully", Toast.LENGTH_SHORT).show();
                }
            });

            mState = 2;
        }


    }


    private void setupAgeCategorySpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter ageCategorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_age_category_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        ageCategorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        ageCategorySpinner.setAdapter(ageCategorySpinnerAdapter);

        // Set the integer mSelected to the constant values
        ageCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("child")) {
                        mAgeCategory = HospitalContract.PatientsEntry.CATEGORY_CHILD; // Male
                    } else if (selection.equals("youth")) {
                        mAgeCategory = HospitalContract.PatientsEntry.CATEGORY_YOUTH; // Female
                    } else if (selection.equals("adult")) {
                        mAgeCategory = HospitalContract.PatientsEntry.CATEGORY_ADULT; // Unknown
                    } else {
                        mAgeCategory = HospitalContract.PatientsEntry.CATEGORY_ELDERLY;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAgeCategory = 0; // Unknown
            }
        });
    }

    private void setupGenderSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PatientsEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PatientsEntry.GENDER_FEMALE; // Female
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }


    private void isEmpty() {
        if (!mNameEdit.getText().toString().trim().matches("") &&
//                !mDoBView.getText().toString().trim().matches("") &&
//                !mDoctorAllocatedEdit.getText().toString().trim().matches("") &&
                !mIllness.getText().toString().trim().matches("")) {
//                !mWardEdit.getText().toString().trim().matches("")) {
            save_btn.setEnabled(true);
            save_changes_btn.setEnabled(true);
        }
    }

    private void isChangedToEmpty(EditText editText) {
        if (editText.getText().toString().trim().matches("")) {
            save_btn.setEnabled(false);
            save_changes_btn.setEnabled(false);
        }
    }


    private void insert() {
        String nameString = mNameEdit.getText().toString().trim();
        Date dateOB = new Date(dateOfBirth_datePicker.getYear() - 1900, dateOfBirth_datePicker.getMonth(),
                dateOfBirth_datePicker.getDayOfMonth());
        long longDoBDate = dateOB.getTime();

        Date dateAD = new Date(admissionDate_datePicker.getYear() - 1900, admissionDate_datePicker.getMonth(),
                admissionDate_datePicker.getDayOfMonth());
        long longRDate = dateAD.getTime();

        String illnessString = mIllness.getText().toString().trim();

        ContentValues values = new ContentValues();

        values.put(PatientsEntry.PATIENT_NAME, nameString);
        values.put(PatientsEntry.DOCTOR_ALLOCATED, doctorAllocatedStr);
        values.put(PatientsEntry.PATIENT_DOB, longDoBDate);
        values.put(PatientsEntry.PATIENT_ILLNESS, illnessString);
        values.put(PatientsEntry.PATIENT_GENDER, mGender);
        values.put(PatientsEntry.PATIENT_AGE_CATEGORY, mAgeCategory);
        values.put(PatientsEntry.WARD_ALLOCATED, wardAllocatedStr);
        values.put(PatientsEntry.ADMISSION_DATE, longRDate);

        getContentResolver().insert(PatientsEntry.CONTENT_URI, values);

    }

    private void setupDoctorsSpinner() {
        try {
            if (cursorD.moveToFirst()) {
                do {
                    String doctorName = cursorD.getString(cursorD.getColumnIndex(HospitalContract.DoctorsEntry.DOCTOR_NAME));
                    doctors.add(doctorName);
                } while (cursorD.moveToNext());
            }
        } finally {
            cursorD.close();
        }

        dataAdapterD
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        doctorSpinner.setAdapter(dataAdapterD);

        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doctorAllocatedStr = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void setupWardSpinner() {

        try {
            if (cursorW.moveToFirst()) {
                do {
                    String wardName = cursorW.getString(cursorW.getColumnIndex(HospitalContract.WardEntry.WARD_NAME));
                    wards.add(wardName);
                } while (cursorW.moveToNext());
            }
        } finally {
            cursorW.close();
        }


        dataAdapterW
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        wardSpinner.setAdapter(dataAdapterW);

        wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wardAllocatedStr = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void update() {

        String nameString = mNameEdit.getText().toString().trim();
        String illnessString = mIllness.getText().toString().trim();

        ContentValues values = new ContentValues();

        values.put(PatientsEntry.PATIENT_NAME, nameString);
        values.put(PatientsEntry.DOCTOR_ALLOCATED, doctorAllocatedStr);
        values.put(PatientsEntry.PATIENT_ILLNESS, illnessString);
        values.put(PatientsEntry.PATIENT_GENDER, mGender);
        values.put(PatientsEntry.PATIENT_AGE_CATEGORY, mAgeCategory);
        values.put(PatientsEntry.WARD_ALLOCATED, wardAllocatedStr);

        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        String selection = PatientsEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};


        getContentResolver().update(PatientsEntry.CONTENT_URI, values, selection, selectionArgs);

    }

    private void delete() {
        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        String selection = PatientsEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

        getContentResolver().delete(PatientsEntry.CONTENT_URI, selection, selectionArgs);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.action_editor, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_delete:
                delete();
                finish();
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_home:
                Intent intent = new Intent(PatientsEditorActivity.this, ContentActivity.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }


}
