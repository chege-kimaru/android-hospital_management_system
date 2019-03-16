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


import java.util.Date;
import java.util.Locale;

import static com.android.kevin.hospitaldatabase.data.HospitalContract.DoctorsEntry;

public class DoctorsEditorActivity extends AppCompatActivity {

    EditText mNameEdit;
    EditText mSpecializationEdit;
    EditText mIdNoEdit;
    EditText mIlnessEdit;
    TextView mIllnessText;
    Spinner mGenderSpinner;
    private int mGender = 0;



    int mState;

    Button save_btn, save_changes_btn;

    DatePicker datepickerDOB;
    DatePicker datepickerReD;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_editor);

        save_btn = (Button) findViewById(R.id.save_btn);
        save_changes_btn = (Button) findViewById(R.id.save_changes_btn);

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

        mSpecializationEdit = (EditText) findViewById(R.id.edit_specialization);
        mSpecializationEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mSpecializationEdit.setError("Field Required");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChangedToEmpty(mSpecializationEdit);
            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmpty();
            }
        });
        mIdNoEdit = (EditText) findViewById(R.id.edit_id_no);
        mIdNoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mIdNoEdit.setError("Field Required");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChangedToEmpty(mIdNoEdit);
            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmpty();
            }
        });
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        datepickerDOB = (DatePicker) findViewById(R.id.datePickerDob);
        datepickerReD = (DatePicker) findViewById(R.id.datePickerRD);

        mIlnessEdit = (EditText) findViewById(R.id.edit_illness);
        mIllnessText = (TextView) findViewById(R.id.text_illness);
        mIlnessEdit.setVisibility(View.GONE);
        mIllnessText.setVisibility(View.GONE);

        setupSpinner();

        //If the intent does not contain a pet content uri , then we know we are creating a new pet
        if (currentUri != null) {
            //This is a new pet so change the appbar to say add pet
            setTitle("Edit Doctor");

            save_btn.setVisibility(View.GONE);
            datepickerReD.setVisibility(View.GONE);
            datepickerDOB.setVisibility(View.GONE);

            save_changes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update();
                    finish();
                    Toast.makeText(DoctorsEditorActivity.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
                }
            });

            mState = 1;


            String selection = DoctorsEntry._ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

            String[] projection = {
                    DoctorsEntry._ID,
                    DoctorsEntry.DOCTOR_NAME,
                    DoctorsEntry.DOCTOR_ID_NO,
                    DoctorsEntry.DOCTOR_GENDER,
                    DoctorsEntry.DOCTOR_SPECIALIZATION,
                    DoctorsEntry.DOCTOR_DOB,
                    DoctorsEntry.DOCTOR_START_DATE
            };

            Cursor cursor = getContentResolver().query(
                    DoctorsEntry.CONTENT_URI,  //The content uri of the words table
                    projection,  //The columns to return for each row
                    selection,   //selection criteria
                    selectionArgs,  //selection criteria
                    null);  //The sort order for the returned rows

            try {

                while (cursor.moveToNext()) {
                    int currentID = cursor.getInt(cursor.getColumnIndex(DoctorsEntry.DOCTOR_ID));
                    String currentName = cursor.getString(cursor.getColumnIndex(DoctorsEntry.DOCTOR_NAME));
                    String currentId_No = cursor.getString(cursor.getColumnIndex(DoctorsEntry.DOCTOR_ID_NO));
                    int current_gender = cursor.getInt(cursor.getColumnIndex(DoctorsEntry.DOCTOR_GENDER));
                    String current_specialization = cursor.getString(cursor.getColumnIndex(DoctorsEntry.DOCTOR_SPECIALIZATION));
                    long current_DoB = cursor.getLong(cursor.getColumnIndex(DoctorsEntry.DOCTOR_DOB));
                    long current_startDate = cursor.getInt(cursor.getColumnIndex(DoctorsEntry.DOCTOR_START_DATE));

                    mNameEdit.setText(currentName);
                    mIdNoEdit.setText(currentId_No);
                    mGenderSpinner.setSelection(current_gender);
                    mSpecializationEdit.setText(current_specialization);
                }
            } finally {
                cursor.close();
            }
        } else {
            mState = 2;
            setTitle("Add doctor");
            save_changes_btn.setVisibility(View.GONE);

            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insert();
                    finish();
                    Toast.makeText(DoctorsEditorActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void setupSpinner() {
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
                        mGender = DoctorsEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = DoctorsEntry.GENDER_FEMALE; // Female
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
                !mSpecializationEdit.getText().toString().trim().matches("") &&
                !mIdNoEdit.getText().toString().trim().matches("")) {
            save_btn.setEnabled(true);
            save_changes_btn.setEnabled(true);
        }
    }

    private void isChangedToEmpty (EditText editText) {
        if (editText.getText().toString().trim().matches("")) {
            save_btn.setEnabled(false);
            save_changes_btn.setEnabled(false);
        }
    }


    private void insert() {
        String nameString = mNameEdit.getText().toString().trim();
        String specializationString = mSpecializationEdit.getText().toString().trim();
        String idNoString = mIdNoEdit.getText().toString().trim();
        Date dateOB = new Date(datepickerDOB.getYear() - 1900, datepickerDOB.getMonth(),
                datepickerDOB.getDayOfMonth());
        long longDoBDate = dateOB.getTime();

        Date dateAD = new Date(datepickerReD.getYear() - 1900, datepickerReD.getMonth(),
                datepickerReD.getDayOfMonth());
        long longRDate = dateAD.getTime();

        ContentValues values = new ContentValues();

        values.put(DoctorsEntry.DOCTOR_NAME, nameString);
        values.put(DoctorsEntry.DOCTOR_ID_NO, idNoString);
        values.put(DoctorsEntry.DOCTOR_DOB, longDoBDate);
        values.put(DoctorsEntry.DOCTOR_GENDER, mGender);
        values.put(DoctorsEntry.DOCTOR_SPECIALIZATION, specializationString);
        values.put(DoctorsEntry.DOCTOR_START_DATE, longRDate);

        getContentResolver().insert(DoctorsEntry.CONTENT_URI, values);
    }

    private void update() {

        String nameString = mNameEdit.getText().toString().trim();
        String specializationString = mSpecializationEdit.getText().toString().trim();
        String idNoString = mIdNoEdit.getText().toString().trim();

        ContentValues values = new ContentValues();

        values.put(DoctorsEntry.DOCTOR_NAME, nameString);
        values.put(DoctorsEntry.DOCTOR_ID_NO, idNoString);
        values.put(DoctorsEntry.DOCTOR_GENDER, mGender);
        values.put(DoctorsEntry.DOCTOR_SPECIALIZATION, specializationString);



        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        String selection = DoctorsEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};


        getContentResolver().update(DoctorsEntry.CONTENT_URI, values, selection, selectionArgs);

    }

    private void delete() {
        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        String selection = DoctorsEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

        getContentResolver().delete(DoctorsEntry.CONTENT_URI, selection, selectionArgs);

        Toast.makeText(this, "Delete successful", Toast.LENGTH_SHORT).show();

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
                Intent intent = new Intent(DoctorsEditorActivity.this, ContentActivity.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }


}
