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

import static com.android.kevin.hospitaldatabase.data.HospitalContract.WardEntry;

/**
 * Created by Kevin Kimaru Chege on 5/6/2017.
 */

public class WardsDbProvider extends ContentProvider {

    private static final int WARDS = 300;
    private static final int WARDS_ID = 301;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer , this is called the first time anything is called from this class

    static {
        sUriMatcher.addURI(HospitalContract.CONTENT_AUTHORITY, HospitalContract.PATH_WARDS, WARDS);
        sUriMatcher.addURI(HospitalContract.CONTENT_AUTHORITY, HospitalContract.PATH_WARDS + "/#", WARDS_ID);
    }

    public static final String LOG_TAG = WardsDbProvider.class.getSimpleName();


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
            case WARDS:
                cursor = database.query(WardEntry.WARD_TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case WARDS_ID:
                selection = HospitalContract.WardEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(WardEntry.WARD_TABLE_NAME, projection, selection, selectionArgs, null,
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
            case WARDS:
                return WardEntry.CONTENT_LIST_TYPE;
            case WARDS_ID:
                return WardEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WARDS:
                return insertWard(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertWard(Uri uri, ContentValues values) {
        //Check that the name is not null
        String name = values.getAsString(WardEntry.WARD_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Ward requires a name.");
        }

        //check that the gender is valid
        Integer gender = values.getAsInteger(WardEntry.WARD_GENDER);
        if (gender == null) {
            throw new IllegalArgumentException("Input a valid gender");
        }

        //Get writable database
        SQLiteDatabase database = mHospitalDbHelper.getWritableDatabase();

        //Insert the new nurse with the given values
        long id = database.insert(WardEntry.WARD_TABLE_NAME, null, values);

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
            case WARDS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(WardEntry.WARD_TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case WARDS_ID:
                // Delete a single row given by the ID in the URI
                selection = WardEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(WardEntry.WARD_TABLE_NAME, selection, selectionArgs);
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
            case WARDS:
                return updateWard(uri, values, selection, selectionArgs);
            case WARDS_ID:
                //Para o código PET_ID, extraia a ID do URI, para que saibamos qual
                // registro atualizar. Selection será "_id=?" e selectionargs será um stringArray
                // contendo o atual ID
                selection = WardEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateWard(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateWard(Uri uri, ContentValues values, String selecion, String[] selectionArgs){

        //Se a chave {@link PetEntry.COLUMN_PET_NAME} é oresente, checar se o valor do
        // nome não é nulo.
        if (values.containsKey(WardEntry.WARD_NAME)){
            String name = values.getAsString(WardEntry.WARD_NAME);
            if (name == null){
                throw new IllegalArgumentException("Ward requires a name");
            }
        }

        //Se a chave {@link PetsEntry.COLUMN_PET_NAME} é oresente, checar se o valor do
        // genero não é nulo.
        if(values.containsKey(WardEntry.WARD_GENDER)){
            Integer gender = values.getAsInteger(WardEntry.WARD_GENDER);
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
        int rowsUpdated = database.update(WardEntry.WARD_TABLE_NAME,values,selecion,selectionArgs);

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
