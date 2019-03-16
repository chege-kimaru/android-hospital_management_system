package com.android.kevin.hospitaldatabase.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kevin Kimaru Chege on 5/5/2017.
 */

public final class HospitalContract {

    private HospitalContract() {}

    public static final String CONTENT_AUTHORITY = "com.android.kevin.hospitaldatabase";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_DOCTORS = "doctors";
    public static final String PATH_NURSES = "nurses";
    public static final String PATH_PATIENTS = "patients";
    public static final String PATH_WARDS = "wards";

    public static final class PatientsEntry implements BaseColumns {
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PATIENTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PATIENTS;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PATIENTS);

        public static final String PATIENTS_TABLE_NAME = "patients";

        public static final String PATIENT_ID = BaseColumns._ID;
        public static final String PATIENT_NAME= "patient_name";
        public static final String PATIENT_DOB= "patient_dob";
        public static final String PATIENT_GENDER= "patient_gender";
        public static final String WARD_ALLOCATED= "ward_allocated";
        public static final String PATIENT_AGE_CATEGORY= "patient_age_category";
        public static final String DOCTOR_ALLOCATED= "doctor_allocated";
        public static final String ADMISSION_DATE= "admission_date";
        public static final String PATIENT_ILLNESS= "patient_illness";

        public static  final int GENDER_MALE = 0;
        public static  final int GENDER_FEMALE = 1;

        public static  final int CATEGORY_CHILD = 0;
        public static  final int CATEGORY_YOUTH = 1;
        public static  final int CATEGORY_ADULT = 2;
        public static  final int CATEGORY_ELDERLY = 3;

    }


    public static final class NursesEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NURSES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NURSES;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NURSES);

        public static final String NURSES_TABLE_NAME = "nurses";

        public static final String NURSES_ID = BaseColumns._ID;
        public static final String NURSE_NAME= "nurse_name";
        public static final String NURSE_ID_NO= "nurse_id_no";
        public static final String NURSE_GENDER= "nurse_gender";
        public static final String NURSE_DOB= "nurse_dob";
        public static final String NURSE_START_DATE= "nurse_start_date";

        public static  final int GENDER_MALE = 0;
        public static  final int GENDER_FEMALE = 1;

    }

    public static final class DoctorsEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOCTORS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOCTORS;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DOCTORS);

        public static final String DOCTORS_TABLE_NAME = "doctors";

        public static final String DOCTOR_ID = BaseColumns._ID;
        public static final String DOCTOR_NAME= "doctor_name";
        public static final String DOCTOR_DOB= "doctor_dob";
        public static final String DOCTOR_ID_NO= "doctor_id_no";
        public static final String DOCTOR_GENDER= "doctor_gender";
        public static final String DOCTOR_SPECIALIZATION= "doctor_specialization";
        public static final String DOCTOR_START_DATE= "doctor_start_date";

        public static  final int GENDER_MALE = 0;
        public static  final int GENDER_FEMALE = 1;

    }

    public static final class WardEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WARDS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WARDS;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_WARDS);

        public static final String WARD_TABLE_NAME = "wards";

        public static final String WARD_ID = BaseColumns._ID;
        public static final String WARD_NAME= "ward_name";
        public static final String WARD_GENDER= "ward_gender";
        public static final String WARD_AGE_CATEGORY= "ward_age_category";
        public static final String WARD_ILLNESS= "ward_illness";

        public static  final int GENDER_MALE = 0;
        public static  final int GENDER_FEMALE = 1;

        public static  final int CATEGORY_CHILD = 0;
        public static  final int CATEGORY_YOUTH = 1;
        public static  final int CATEGORY_ADULT = 2;
        public static  final int CATEGORY_ELDERLY = 3;
    }
}
