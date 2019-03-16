package com.android.kevin.hospitaldatabase.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.android.kevin.hospitaldatabase.data.HospitalContract.DoctorsEntry;

/**
 * Created by Kevin Kimaru Chege on 5/6/2017.
 */

public class DoctorsDbProvider extends ContentProvider {

    private static final int DOCTORS = 100;
    private static final int DOCTORS_ID = 101;
    private static final int NURSES = 200;
    private static final int NURSES_ID = 201;
    private static final int WARDS = 300;
    private static final int WARDS_ID = 301;
    private static final int PATIENTS = 400;
    private static final int PATIENTS_ID = 401;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer , this is called the first time anything is called from this class
    static {
        sUriMatcher.addURI(HospitalContract.CONTENT_AUTHORITY, HospitalContract.PATH_DOCTORS, DOCTORS);
        sUriMatcher.addURI(HospitalContract.CONTENT_AUTHORITY, HospitalContract.PATH_DOCTORS + "/#", DOCTORS_ID);
        sUriMatcher.addURI(HospitalContract.CONTENT_AUTHORITY, HospitalContract.PATH_NURSES, NURSES);
        sUriMatcher.addURI(HospitalContract.CONTENT_AUTHORITY, HospitalContract.PATH_NURSES + "/#", NURSES_ID);
        sUriMatcher.addURI(HospitalContract.CONTENT_AUTHORITY, HospitalContract.PATH_WARDS, WARDS);
        sUriMatcher.addURI(HospitalContract.CONTENT_AUTHORITY, HospitalContract.PATH_WARDS + "/#", WARDS_ID);
        sUriMatcher.addURI(HospitalContract.CONTENT_AUTHORITY, HospitalContract.PATH_PATIENTS, PATIENTS);
        sUriMatcher.addURI(HospitalContract.CONTENT_AUTHORITY, HospitalContract.PATH_PATIENTS + "/#", PATIENTS_ID);
    }

    public static final String LOG_TAG = DoctorsDbProvider.class.getSimpleName();


    private HospitalDbHelper mHospitalDbHelper;

    @Override
    public boolean onCreate() {

        mHospitalDbHelper = new HospitalDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mHospitalDbHelper.getReadableDatabase();

        //This cursor will hol;d the result of the query
        Cursor cursor;

        //figure out if the uri can match the uri to a specific record
        int match = sUriMatcher.match(uri);
        switch (match) {
            case DOCTORS:
                cursor = database.query(DoctorsEntry.DOCTORS_TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case DOCTORS_ID:
                selection = DoctorsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DoctorsEntry.DOCTORS_TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case NURSES:
                cursor = database.query(HospitalContract.NursesEntry.NURSES_TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case NURSES_ID:
                selection = HospitalContract.NursesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(HospitalContract.NursesEntry.NURSES_TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case WARDS:
                cursor = database.query(HospitalContract.WardEntry.WARD_TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case WARDS_ID:
                selection = HospitalContract.WardEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(HospitalContract.WardEntry.WARD_TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case PATIENTS:
                cursor = database.query(HospitalContract.PatientsEntry.PATIENTS_TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case PATIENTS_ID:
                selection = HospitalContract.PatientsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(HospitalContract.PatientsEntry.PATIENTS_TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unkown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DOCTORS:
                return DoctorsEntry.CONTENT_LIST_TYPE;
            case DOCTORS_ID:
                return DoctorsEntry.CONTENT_ITEM_TYPE;
            case NURSES:
                return HospitalContract.NursesEntry.CONTENT_LIST_TYPE;
            case NURSES_ID:
                return HospitalContract.NursesEntry.CONTENT_ITEM_TYPE;
            case PATIENTS:
                return HospitalContract.PatientsEntry.CONTENT_LIST_TYPE;
            case PATIENTS_ID:
                return HospitalContract.PatientsEntry.CONTENT_ITEM_TYPE;
            case WARDS:
                return HospitalContract.WardEntry.CONTENT_LIST_TYPE;
            case WARDS_ID:
                return HospitalContract.WardEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DOCTORS:
                return insertDoctor(uri, values);
            case NURSES:
                return insertNurse(uri, values);
            case PATIENTS:
                return insertPatient(uri, values);
            case WARDS:
                return insertWard(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertWard(Uri uri, ContentValues values) {
        //Check that the name is not null
        String name = values.getAsString(HospitalContract.WardEntry.WARD_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Ward requires a name.");
        }

        //check that the gender is valid
        Integer gender = values.getAsInteger(HospitalContract.WardEntry.WARD_GENDER);
        if (gender == null) {
            throw new IllegalArgumentException("Input a valid gender");
        }

        //Get writable database
        SQLiteDatabase database = mHospitalDbHelper.getWritableDatabase();

        //Insert the new nurse with the given values
        long id = database.insert(HospitalContract.WardEntry.WARD_TABLE_NAME, null, values);

        //if the id is -1, then the insertion failed .Log an error and return null
        if (id == -1) {
            Log.e(LOG_TAG, "failed to return a new row for " + uri);
            return null;
        }

        //notify all listeners that the data has changed for the pet content uri
        getContext().getContentResolver().notifyChange(uri, null);

        //return the new uri with the new id appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertPatient(Uri uri, ContentValues values) {
        //Check that the name is not null
        String name = values.getAsString(HospitalContract.PatientsEntry.PATIENT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Patient requires a name.");
        }

        //check that the gender is valid
        Integer gender = values.getAsInteger(HospitalContract.PatientsEntry.PATIENT_GENDER);
        if (gender == null) {
            throw new IllegalArgumentException("Input a valid gender");
        }

        //Get writable database
        SQLiteDatabase database = mHospitalDbHelper.getWritableDatabase();

        //Insert the new nurse with the given values
        long id = database.insert(HospitalContract.PatientsEntry.PATIENTS_TABLE_NAME, null, values);

        //if the id is -1, then the insertion failed .Log an error and return null
        if (id == -1) {
            Log.e(LOG_TAG, "failed to return a new row for " + uri);
            return null;
        }

        //notify all listeners that the data has changed for the pet content uri
        getContext().getContentResolver().notifyChange(uri, null);

        //return the new uri with the new id appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertNurse(Uri uri, ContentValues values) {
        //Check that the name is not null
        String name = values.getAsString(HospitalContract.NursesEntry.NURSE_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Nurse requires a name.");
        }

        String idNo = values.getAsString(HospitalContract.NursesEntry.NURSE_ID_NO);
        if (idNo == null) {
            throw new IllegalArgumentException("Nurse requires an ID number");
        }

        //check that the gender is valid
        Integer gender = values.getAsInteger(HospitalContract.NursesEntry.NURSE_GENDER);
        if (gender == null) {
            throw new IllegalArgumentException("Input a valid gender");
        }

        Integer startDtae = values.getAsInteger(HospitalContract.NursesEntry.NURSE_START_DATE);
        if (startDtae == null) {
            throw new IllegalArgumentException("Input date of reporting");
        }


        //Get writable database
        SQLiteDatabase database = mHospitalDbHelper.getWritableDatabase();

        //Insert the new nurse with the given values
        long id = database.insert(HospitalContract.NursesEntry.NURSES_TABLE_NAME, null, values);

        //if the id is -1, then the insertion failed .Log an error and return null
        if (id == -1) {
            Log.e(LOG_TAG, "failed to return a new row for " + uri);
            return null;
        }

        //notify all listeners that the data has changed for the pet content uri
        getContext().getContentResolver().notifyChange(uri, null);

        //return the new uri with the new id appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertDoctor(Uri uri, ContentValues values) {
        //Check that the name is not null
        String name = values.getAsString(DoctorsEntry.DOCTOR_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Doctor requires a name.");
        }

        String idNo = values.getAsString(DoctorsEntry.DOCTOR_ID_NO);
        if (idNo == null) {
            throw new IllegalArgumentException("Doctor requires an ID number");
        }

        String specialization = values.getAsString(DoctorsEntry.DOCTOR_SPECIALIZATION);
        if (specialization == null) {
            throw new IllegalArgumentException("Define doctor's specialization");
        }

        //check that the gender is valid
        Integer gender = values.getAsInteger(DoctorsEntry.DOCTOR_GENDER);
        if (gender == null) {
            throw new IllegalArgumentException("Input a valid gender");
        }

        Integer startDtae = values.getAsInteger(DoctorsEntry.DOCTOR_START_DATE);
        if (startDtae == null) {
            throw new IllegalArgumentException("Input date of reporting");
        }

        //No need to check breed,any value is valid including null

        //Get writable database
        SQLiteDatabase database = mHospitalDbHelper.getWritableDatabase();

        //Insert the new pet with the given values
        long id = database.insert(DoctorsEntry.DOCTORS_TABLE_NAME, null, values);

        //if the id is -1, then the insertion failed .Log an error and return null
        if (id == -1) {
            Log.e(LOG_TAG, "failed to return a new row for " + uri);
            return null;
        }

        //notify all listeners that the data has changed for the pet content uri
        getContext().getContentResolver().notifyChange(uri, null);

        //return the new uri with the new id appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        // Track the number of rows that were deleted
        int rowsDeleted;
        // Get writeable database
        SQLiteDatabase database = mHospitalDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DOCTORS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(DoctorsEntry.DOCTORS_TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case DOCTORS_ID:
                // Delete a single row given by the ID in the URI
                selection = DoctorsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(DoctorsEntry.DOCTORS_TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case WARDS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(HospitalContract.WardEntry.WARD_TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case WARDS_ID:
                // Delete a single row given by the ID in the URI
                selection = HospitalContract.WardEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(HospitalContract.WardEntry.WARD_TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case NURSES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(HospitalContract.NursesEntry.NURSES_TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case NURSES_ID:
                // Delete a single row given by the ID in the URI
                selection = HospitalContract.NursesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(HospitalContract.NursesEntry.NURSES_TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case PATIENTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(HospitalContract.PatientsEntry.PATIENTS_TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case PATIENTS_ID:
                // Delete a single row given by the ID in the URI
                selection = HospitalContract.PatientsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(HospitalContract.PatientsEntry.PATIENTS_TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case DOCTORS:
                return updateDoctor(uri, values, selection, selectionArgs);
            case DOCTORS_ID:
                //Para o código PET_ID, extraia a ID do URI, para que saibamos qual
                // registro atualizar. Selection será "_id=?" e selectionargs será um stringArray
                // contendo o atual ID
                selection = DoctorsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateDoctor(uri, values, selection, selectionArgs);
            case NURSES:
                return updateNurse(uri, values, selection, selectionArgs);
            case NURSES_ID:
                //Para o código PET_ID, extraia a ID do URI, para que saibamos qual
                // registro atualizar. Selection será "_id=?" e selectionargs será um stringArray
                // contendo o atual ID
                selection = HospitalContract.NursesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNurse(uri, values, selection, selectionArgs);
            case WARDS:
                return updateWard(uri, values, selection, selectionArgs);
            case WARDS_ID:
                //Para o código PET_ID, extraia a ID do URI, para que saibamos qual
                // registro atualizar. Selection será "_id=?" e selectionargs será um stringArray
                // contendo o atual ID
                selection = HospitalContract.WardEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateWard(uri, values, selection, selectionArgs);
            case PATIENTS:
                return updatePatient(uri, values, selection, selectionArgs);
            case PATIENTS_ID:
                //Para o código PET_ID, extraia a ID do URI, para que saibamos qual
                // registro atualizar. Selection será "_id=?" e selectionargs será um stringArray
                // contendo o atual ID
                selection = HospitalContract.PatientsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePatient(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateWard(Uri uri, ContentValues values, String selecion, String[] selectionArgs){

        //Se a chave {@link PetEntry.COLUMN_PET_NAME} é oresente, checar se o valor do
        // nome não é nulo.
        if (values.containsKey(HospitalContract.WardEntry.WARD_NAME)){
            String name = values.getAsString(HospitalContract.WardEntry.WARD_NAME);
            if (name == null){
                throw new IllegalArgumentException("Ward requires a name");
            }
        }

        //Se a chave {@link PetsEntry.COLUMN_PET_NAME} é oresente, checar se o valor do
        // genero não é nulo.
        if(values.containsKey(HospitalContract.WardEntry.WARD_GENDER)){
            Integer gender = values.getAsInteger(HospitalContract.WardEntry.WARD_GENDER);
            if (gender == null){
                throw new IllegalArgumentException("Ward requires a gender");
            }
        }

        //Se não há valores para autalizar, então atualize o banco de dados
        if (values.size() == 0){
            return 0;
        }

        //Caso contrario, obtém o banco de dados com permissão de escrita para atualizar os dados.
        SQLiteDatabase database = mHospitalDbHelper.getWritableDatabase();

        //Executa a atualização no banco de dados e obtém o número de linhas afetadas
        int rowsUpdated = database.update(HospitalContract.WardEntry.WARD_TABLE_NAME,values,selecion,selectionArgs);

        //Se 1 ou mais linhas foram atualizadas, então notifica todos os listeners que os
        // dados da URI mudaram
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
        //Retorna o número de registros do banco de dados afetados pelo comando update
        //return database.update(PetsEntry.TABLE_NAME, values, selecion, selectionArgs);
    }

    private int updatePatient(Uri uri, ContentValues values, String selecion, String[] selectionArgs){

        //Se a chave {@link PetEntry.COLUMN_PET_NAME} é oresente, checar se o valor do
        // nome não é nulo.
        if (values.containsKey(HospitalContract.PatientsEntry.PATIENT_NAME)){
            String name = values.getAsString(HospitalContract.PatientsEntry.PATIENT_NAME);
            if (name == null){
                throw new IllegalArgumentException("Patient requires a name");
            }
        }

        //Se a chave {@link PetsEntry.COLUMN_PET_NAME} é oresente, checar se o valor do
        // genero não é nulo.
        if(values.containsKey(HospitalContract.PatientsEntry.PATIENT_GENDER)){
            Integer gender = values.getAsInteger(HospitalContract.PatientsEntry.PATIENT_GENDER);
            if (gender == null){
                throw new IllegalArgumentException("Nurse requires a gender");
            }
        }

        //Se não há valores para autalizar, então atualize o banco de dados
        if (values.size() == 0){
            return 0;
        }

        //Caso contrario, obtém o banco de dados com permissão de escrita para atualizar os dados.
        SQLiteDatabase database = mHospitalDbHelper.getWritableDatabase();

        //Executa a atualização no banco de dados e obtém o número de linhas afetadas
        int rowsUpdated = database.update(HospitalContract.PatientsEntry.PATIENTS_TABLE_NAME,values,selecion,selectionArgs);

        //Se 1 ou mais linhas foram atualizadas, então notifica todos os listeners que os
        // dados da URI mudaram
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
        //Retorna o número de registros do banco de dados afetados pelo comando update
        //return database.update(PetsEntry.TABLE_NAME, values, selecion, selectionArgs);
    }

    private int updateNurse(Uri uri, ContentValues values, String selecion, String[] selectionArgs){

        //Se a chave {@link PetEntry.COLUMN_PET_NAME} é oresente, checar se o valor do
        // nome não é nulo.
        if (values.containsKey(HospitalContract.NursesEntry.NURSE_NAME)){
            String name = values.getAsString(HospitalContract.NursesEntry.NURSE_NAME);
            if (name == null){
                throw new IllegalArgumentException("Nurse requires a name");
            }
        }

        //Se a chave {@link PetsEntry.COLUMN_PET_NAME} é oresente, checar se o valor do
        // genero não é nulo.
        if(values.containsKey(HospitalContract.NursesEntry.NURSE_GENDER)){
            Integer gender = values.getAsInteger(HospitalContract.NursesEntry.NURSE_GENDER);
            if (gender == null){
                throw new IllegalArgumentException("Nurse requires a gender");
            }
        }

        //Se não há valores para autalizar, então atualize o banco de dados
        if (values.size() == 0){
            return 0;
        }

        //Caso contrario, obtém o banco de dados com permissão de escrita para atualizar os dados.
        SQLiteDatabase database = mHospitalDbHelper.getWritableDatabase();

        //Executa a atualização no banco de dados e obtém o número de linhas afetadas
        int rowsUpdated = database.update(HospitalContract.NursesEntry.NURSES_TABLE_NAME,values,selecion,selectionArgs);

        //Se 1 ou mais linhas foram atualizadas, então notifica todos os listeners que os
        // dados da URI mudaram
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
        //Retorna o número de registros do banco de dados afetados pelo comando update
        //return database.update(PetsEntry.TABLE_NAME, values, selecion, selectionArgs);
    }

    private int updateDoctor(Uri uri, ContentValues values, String selecion, String[] selectionArgs){

        //Se a chave {@link PetEntry.COLUMN_PET_NAME} é oresente, checar se o valor do
        // nome não é nulo.
        if (values.containsKey(DoctorsEntry.DOCTOR_NAME)){
            String name = values.getAsString(DoctorsEntry.DOCTOR_NAME);
            if (name == null){
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        //Se a chave {@link PetsEntry.COLUMN_PET_NAME} é oresente, checar se o valor do
        // genero não é nulo.
        if(values.containsKey(DoctorsEntry.DOCTOR_GENDER)){
            Integer gender = values.getAsInteger(DoctorsEntry.DOCTOR_GENDER);
            if (gender == null){
                throw new IllegalArgumentException("Pet requires a gender");
            }
        }

        //Se não há valores para autalizar, então atualize o banco de dados
        if (values.size() == 0){
            return 0;
        }

        //Caso contrario, obtém o banco de dados com permissão de escrita para atualizar os dados.
        SQLiteDatabase database = mHospitalDbHelper.getWritableDatabase();

        //Executa a atualização no banco de dados e obtém o número de linhas afetadas
        int rowsUpdated = database.update(DoctorsEntry.DOCTORS_TABLE_NAME,values,selecion,selectionArgs);

        //Se 1 ou mais linhas foram atualizadas, então notifica todos os listeners que os
        // dados da URI mudaram
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
        //Retorna o número de registros do banco de dados afetados pelo comando update
        //return database.update(PetsEntry.TABLE_NAME, values, selecion, selectionArgs);
    }
}
