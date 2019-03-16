package com.android.kevin.hospitaldatabase;


import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.android.kevin.hospitaldatabase.data.HospitalContract;

import static com.android.kevin.hospitaldatabase.WardCursorAdapter.SelectedWardCursor;

import static com.android.kevin.hospitaldatabase.data.HospitalContract.WardEntry;

public class WardsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private int mYear, mMonth, mDay;
    private static final int WARD_LOADER = 3;

    SelectedWardCursor mCursorAdapter = new SelectedWardCursor(this, null);

    final String[] choices = {"Edit this Ward's Details", "View this Ward's details", "Delete"};
    final String[] choices2 = {"Yes", "No"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);
        //View and set empty view on the listView so that it only shows when the list has 0 items

        mCursorAdapter = new SelectedWardCursor(this, null);
        listView.setAdapter(mCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(WardsActivity.this);
                builder.setTitle("Select your choioce")
                        .setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //start gameplay
                                if (which == 0) {
                                    //Create new pet to go to editor activity
                                    Intent intent = new Intent(WardsActivity.this, WardsEditorActivity.class);

                                    //Form the current uri that represents the specific pet that was clicked on by
                                    //appending the id (pased as input to this method) onto the (@link PetEntry#CONTENT_URI)
                                    //for example the uri would be  "content:// com.android.pets/pets/2" if the pet with id 2 was clicked

                                    Uri currentUri = ContentUris.withAppendedId(HospitalContract.WardEntry.CONTENT_URI, id);

                                    //set the uri on the data field of the intent
                                    intent.setData(currentUri);

                                    //launch the (@link editor activity to display the data for the current pet)
                                    startActivity(intent);
                                } else if (which == 1) {
                                    Intent intent = new Intent(WardsActivity.this, AllWardDetailsActivity.class);
                                    Uri currentUri = ContentUris.withAppendedId(HospitalContract.WardEntry.CONTENT_URI, id);

                                    //set the uri on the data field of the intent
                                    intent.setData(currentUri);
                                    startActivity(intent);
                                } else if (which == 2) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(WardsActivity.this);
                                    builder.setTitle("Are you sure you want to delete this item!")
                                            .setSingleChoiceItems(choices2, 0, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    if (which == 0) {
                                                        Uri currentUri = ContentUris.withAppendedId(HospitalContract.WardEntry.CONTENT_URI, id);
                                                        String selection = WardEntry._ID + "=?";
                                                        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};

                                                        getContentResolver().delete(WardEntry.CONTENT_URI, selection, selectionArgs);
                                                    }

                                                }
                                            });
                                    AlertDialog ad = builder.create();
                                    ad.show();
                                }
                            }

                        });
                AlertDialog ad = builder.create();
                ad.show();

            }
        });

        getSupportLoaderManager().initLoader(WARD_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                WardEntry._ID,
                WardEntry.WARD_NAME,
                WardEntry.WARD_ILLNESS,
        };

        return new CursorLoader(this,  //Parent activity context
                WardEntry.CONTENT_URI, //Provider content uri to query
                projection,  //Columns to include in the resulting Cursor
                null,   //No selection clause
                null,   //No selectiom args
                null);      //Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor_insert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_insert) {
            Intent intent = new Intent(WardsActivity.this, WardsEditorActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
