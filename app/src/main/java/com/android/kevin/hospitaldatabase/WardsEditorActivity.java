package com.android.kevin.hospitaldatabase;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.print.PrintHelper;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import static com.android.kevin.hospitaldatabase.data.HospitalContract.WardEntry;

public class WardsEditorActivity extends AppCompatActivity {

    EditText mNameEdit, mAgeCategoryEdit, mIllnessEdit, mIdNoEdit, mStartDateEdit, mEndDateEdit,
            mDateOfBirthEdit, mSpecializationEdit;
    TextView mNameText, mAgeCategoryText, mIllnessText, mStartDateText, mEndDateText,
            mGenderText, mDateOfBirthText, mSpecializationText;
    Spinner mGenderSpinner;
    Spinner mAgeCategorySpinner;
    private int mGender = 0;
    private int mAgeCategory = 1;
    Button pickReportingDate;
    Button pickRetireDate;

    Button save_btn, save_changes_btn;

    int mState = 0;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wards_editor);

        final Intent intent = getIntent();
        Uri currentUri = intent.getData();


        mNameEdit = (EditText) findViewById(R.id.edit_name);
        mNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mNameEdit.setError("Required");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChangedToEmpty(mNameEdit);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mNameEdit.getText().toString().trim().matches("") &&
                        !mIllnessEdit.getText().toString().trim().matches("")) {
//                        !mAgeCategoryEdit.getText().toString().trim().matches("")) {
                    save_btn.setEnabled(true);
                    save_changes_btn.setEnabled(true);
                }
            }
        });
        //      mAgeCategoryEdit = (EditText) findViewById(R.id.edit_id_no);
//        mAgeCategoryEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                mAgeCategoryEdit.setError("Required");
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                isChangedToEmpty(mAgeCategoryEdit);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!mNameEdit.getText().toString().trim().matches("") &&
//                        !mIllnessEdit.getText().toString().trim().matches("") &&
//                        !mAgeCategoryEdit.getText().toString().trim().matches("")) {
//                    save_btn.setEnabled(true);
//                    save_changes_btn.setEnabled(true);
//                }
//            }
//        });
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mAgeCategorySpinner = (Spinner) findViewById(R.id.spinner_ageCategory);
        mIllnessEdit = (EditText) findViewById(R.id.edit_illness);
        mIllnessEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mIllnessEdit.setError("Required");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChangedToEmpty(mIllnessEdit);
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!mNameEdit.getText().toString().trim().matches("") &&
                        !mIllnessEdit.getText().toString().trim().matches("")) {
//                        !mAgeCategoryEdit.getText().toString().trim().matches("")) {
                    save_btn.setEnabled(true);
                    save_changes_btn.setEnabled(true);
                }
            }
        });

        mDateOfBirthEdit = (EditText) findViewById(R.id.edit_date_of_birth);
        mStartDateEdit = (EditText) findViewById(R.id.edit_date_of_report);
        mEndDateEdit = (EditText) findViewById(R.id.edit_retirement_date);
        mSpecializationEdit = (EditText) findViewById(R.id.edit_specialization);

        mDateOfBirthEdit.setVisibility(View.GONE);
        mStartDateEdit.setVisibility(View.GONE);
        mEndDateEdit.setVisibility(View.GONE);
        mSpecializationEdit.setVisibility(View.GONE);


        mNameText = (TextView) findViewById(R.id.text_name);
        mAgeCategoryText = (TextView) findViewById(R.id.text_id_no);
        mGenderText = (TextView) findViewById(R.id.text_gender);
        mIllnessText = (TextView) findViewById(R.id.text_illness);

        mStartDateText = (TextView) findViewById(R.id.text_date_of_report);
        mEndDateText = (TextView) findViewById(R.id.text_retirement_date);
        mDateOfBirthText = (TextView) findViewById(R.id.text_date_of_birth);
        mSpecializationText = (TextView) findViewById(R.id.text_specialization);

        mAgeCategoryText.setText("Age Category");
        mDateOfBirthText.setVisibility(View.GONE);
        mStartDateText.setVisibility(View.GONE);
        mEndDateText.setVisibility(View.GONE);
        mSpecializationText.setVisibility(View.GONE);

        save_btn = (Button) findViewById(R.id.save_btn);
        save_changes_btn = (Button) findViewById(R.id.save_changes_btn);
        pickReportingDate = (Button) findViewById(R.id.btn_report);
        pickRetireDate = (Button) findViewById(R.id.btn_retire);

        pickReportingDate.setVisibility(View.GONE);
        pickRetireDate.setVisibility(View.GONE);


        setupGenderSpinner();
        setupAgeCategorySpinner();

        //If the intent does not contain a pet content uri , then we know we are creating a new pet
        if (currentUri != null) {
            //This is a new pet so change the appbar to say add pet
            setTitle("Edit Ward");
            save_btn.setVisibility(View.GONE);

            save_changes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update();
                    Intent intent1 = new Intent(WardsEditorActivity.this, WardsActivity.class);
                    startActivity(intent1);
                }
            });

            String selection = WardEntry._ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

            String[] projection = {
                    WardEntry._ID,
                    WardEntry.WARD_NAME,
                    WardEntry.WARD_ILLNESS,
                    WardEntry.WARD_GENDER,
                    WardEntry.WARD_AGE_CATEGORY,
            };

            Cursor cursor = getContentResolver().query(
                    WardEntry.CONTENT_URI,  //The content uri of the words table
                    projection,  //The columns to return for each row
                    selection,   //selection criteria
                    selectionArgs,  //selection criteria
                    null);  //The sort order for the returned rows

            try {

                while (cursor.moveToNext()) {
                    String currentName = cursor.getString(cursor.getColumnIndex(WardEntry.WARD_NAME));
                    int current_gender = cursor.getInt(cursor.getColumnIndex(WardEntry.WARD_GENDER));
                    String current_illness = cursor.getString(cursor.getColumnIndex(WardEntry.WARD_ILLNESS));
                    int current_age_category = cursor.getInt(cursor.getColumnIndex(WardEntry.WARD_AGE_CATEGORY));

                    mNameEdit.setText(currentName);
                    mGenderSpinner.setSelection(current_gender);
                    mAgeCategorySpinner.setSelection(current_age_category);
                    mIllnessEdit.setText(current_illness);
                }
            } finally {
                cursor.close();
            }
        } else {
            setTitle("Add Ward");
            save_changes_btn.setVisibility(View.GONE);
            invalidateOptionsMenu();
            mState = 1;

            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insert();
                    finish();
                }
            });
        }
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
                        mGender = WardEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = WardEntry.GENDER_FEMALE; // Female
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

    private void setupAgeCategorySpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter ageCategorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_age_category_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        ageCategorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mAgeCategorySpinner.setAdapter(ageCategorySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mAgeCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("child")) {
                        mAgeCategory = WardEntry.CATEGORY_CHILD; // Male
                    } else if (selection.equals("youth")) {
                        mAgeCategory = WardEntry.CATEGORY_YOUTH; // Female
                    } else if (selection.equals("adult")) {
                        mAgeCategory = WardEntry.CATEGORY_ADULT; // Unknown
                    } else {
                        mAgeCategory = WardEntry.CATEGORY_ELDERLY;
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

    private void isChangedToEmpty(EditText editText) {
        if (editText.getText().toString().trim().matches("")) {
            save_btn.setEnabled(false);
            save_changes_btn.setEnabled(false);
        }
    }


    private void insert() {
        String nameString = mNameEdit.getText().toString().trim();
        //String ageCategoryString = mAgeCategoryEdit.getText().toString().trim();
        String illnessString = mIllnessEdit.getText().toString().trim();

        ContentValues values = new ContentValues();

        values.put(WardEntry.WARD_NAME, nameString);
        values.put(WardEntry.WARD_AGE_CATEGORY, mAgeCategory);
        values.put(WardEntry.WARD_GENDER, mGender);
        values.put(WardEntry.WARD_ILLNESS, illnessString);

        getContentResolver().insert(WardEntry.CONTENT_URI, values);

    }

    private void update() {

        String nameString = mNameEdit.getText().toString().trim();
//        String ageCategoryString = mAgeCategoryEdit.getText().toString().trim();
        String illnessString = mIllnessEdit.getText().toString().trim();

        ContentValues values = new ContentValues();

        values.put(WardEntry.WARD_NAME, nameString);
        values.put(WardEntry.WARD_AGE_CATEGORY, mAgeCategory);
        values.put(WardEntry.WARD_GENDER, mGender);
        values.put(WardEntry.WARD_ILLNESS, illnessString);

        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        String selection = WardEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};


        getContentResolver().update(WardEntry.CONTENT_URI, values, selection, selectionArgs);

    }

    private void delete() {
        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        String selection = WardEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

        getContentResolver().delete(WardEntry.CONTENT_URI, selection, selectionArgs);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.action_editor, menu);

        if (mState == 1) {
            menu.getItem(0).setVisible(false);
        }

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
                Intent intent = new Intent(WardsEditorActivity.this, ContentActivity.class);
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


}
