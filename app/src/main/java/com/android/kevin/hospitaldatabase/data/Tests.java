package com.android.kevin.hospitaldatabase.data;

/**
 * Created by Kevin Kimaru Chege on 5/5/2017.
 */

public class Tests {}
        //First you need to create multiple CONTENT_URIs

//    public static final Uri CONTENT_URI1 =
//            Uri.parse("content://"+ PROVIDER_NAME + "/sampleuri1");
//    public static final Uri CONTENT_URI2 =
//            Uri.parse("content://"+ PROVIDER_NAME + "/sampleuri2");

        //Then you expand your URI Matcher

//        private static final UriMatcher uriMatcher;
//    static {
//        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        uriMatcher.addURI(PROVIDER_NAME, "sampleuri1", SAMPLE1);
//        uriMatcher.addURI(PROVIDER_NAME, "sampleuri1/#", SAMPLE1_ID);
//        uriMatcher.addURI(PROVIDER_NAME, "sampleuri2", SAMPLE2);
//        uriMatcher.addURI(PROVIDER_NAME, "sampleuri2/#", SAMPLE2_ID);
//    }

        //Then create your tables

//    private static final String DATABASE_NAME = "sample.db";
//    private static final String DATABASE_TABLE1 = "sample1";
//    private static final String DATABASE_TABLE2 = "sample2";
//    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_CREATE1 =
//            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE1 +
//                    " (" + _ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "data text, stuff text);";
//    private static final String DATABASE_CREATE2 =
//            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE2 +
//                    " (" + _ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "data text, stuff text);";
        //Don't forget to add the second DATABASE_CREATE to onCreate()


    //You are going to use a switch-case block to determine what table is used. This is my insert code

//    @Override
//    public Uri insert(Uri uri, ContentValues values) {
//        Uri _uri = null;
//        switch (uriMatcher.match(uri)){
//            case SAMPLE1:
//                long _ID1 = db.insert(DATABASE_TABLE1, "", values);
//                //---if added successfully---
//                if (_ID1 > 0) {
//                    _uri = ContentUris.withAppendedId(CONTENT_URI1, _ID1);
//                    getContext().getContentResolver().notifyChange(_uri, null);
//                }
//                break;
//            case SAMPLE2:
//                long _ID2 = db.insert(DATABASE_TABLE2, "", values);
//                //---if added successfully---
//                if (_ID2 > 0) {
//                    _uri = ContentUris.withAppendedId(CONTENT_URI2, _ID2);
//                    getContext().getContentResolver().notifyChange(_uri, null);
//                }
//                break;
//            default: throw new SQLException("Failed to insert row into " + uri);
//        }
//        return _uri;
//    }





    //dates
    //values.put(DATE, showDate.getText().toString() );


//    public static Long persistDate(Date date) {
//        if (date != null) {
//            return date.getTime();
//        }
//        return null;
//    }

//
//    ContentValues values = new ContentValues();
//values.put(COLUMN_NAME, persistDate(entity.getDate()));
//    long id = db.insertOrThrow(TABLE_NAME, null, values);

//    public static Date loadDate(Cursor cursor, int index) {
//        if (cursor.isNull(index)) {
//            return null;
//        }
//        return new Date(cursor.getLong(index));
//    }


//    entity.setDate(loadDate(cursor, INDEX));



//
//    public static final String QUERY = "SELECT table._id, table.dateCol FROM table ORDER BY table.dateCol DESC";
//
////...
//
//    Cursor cursor = rawQuery(QUERY, null);
//    cursor.moveToFirst();
//
//    while (!cursor.isAfterLast()) {
//        // Process results
 //   }



//    Calendar calendar = Calendar.getInstance();
//calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
//        timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
//    long startTime = calendar.getTimeInMillis();



//}
//
//
//    <?xml version="1.0" encoding="utf-8"?>
//<translate
//                  xmlns:android="http://schemas.android.com/apk/res/android"
//                          android:duration="@android:integer/config_longAnimTime"
//                          android:fromXDelta="100%p"
//                          android:toXDelta="0%p">
//</translate>

//    <?xml version="1.0" encoding="utf-8"?>
//           <translate
//    xmlns:android="http://schemas.android.com/apk/res/android"
//    android:duration="@android:integer/config_longAnimTime"
//    android:fromXDelta="0%p"
//    android:toXDelta="-100%p">
//          </translate>

   // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splashscreen);
//
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//
//                     /* Create an intent that will start the main activity. */
//                Intent mainIntent = new Intent(SplashScreen.this,
//                        ConnectedActivity.class);
//                mainIntent.putExtra("id", "1");
//
//                //SplashScreen.this.startActivity(mainIntent);
//                startActivity(mainIntent);
//                     /* Finish splash activity so user cant go back to it. */
//                SplashScreen.this.finish();
//
//                     /* Apply our splash exit (fade out) and main
//                        entry (fade in) animation transitions. */
//                overridePendingTransition(R.anim.mainfadein,R.anim.splashfadeout);
//            }
//        }, SPLASH_DISPLAY_TIME);
//    }




//<Spinner
//        android:id="@+id/spinner"
//                android:layout_width="fill_parent"
//                android:layout_height="wrap_content"
//                android:prompt="@string/spinner_title"
//                android:layout_marginTop="20dip"
//                android:layout_marginLeft="8dip"
//                android:layout_marginRight="8dip"
//                />