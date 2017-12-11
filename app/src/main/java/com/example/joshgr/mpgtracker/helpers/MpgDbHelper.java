package com.example.joshgr.mpgtracker.helpers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.joshgr.mpgtracker.data.TripDataItem;

import java.util.ArrayList;
import java.util.Date;

public class MpgDbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "MPG_SQLite.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TRIPS_TABLE_NAME = "trips";
    public static final String TRIPS_COLUMN_ID = "_id";
    public static final String TRIPS_COLUMN_DATE = "date";
    public static final String TRIPS_COLUMN_GALLONS = "gallons";
    public static final String TRIPS_COLUMN_MILES = "miles";
    public static final String TRIPS_COLUMN_COST = "cost";

    public MpgDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TRIPS_TABLE_NAME + "(" +
            TRIPS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            TRIPS_COLUMN_DATE + " TEXT, " +
            TRIPS_COLUMN_GALLONS + " REAL, " +
            TRIPS_COLUMN_MILES + " REAL," +
            TRIPS_COLUMN_COST + " REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TRIPS_TABLE_NAME);
        onCreate(db);
    }

    public void addTrip(TripDataItem trip){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRIPS_COLUMN_DATE, trip.getDate().toString());
        contentValues.put(TRIPS_COLUMN_GALLONS, trip.getGallons());
        contentValues.put(TRIPS_COLUMN_MILES, trip.getMiles());
        contentValues.put(TRIPS_COLUMN_COST, trip.getTripCost());
        db.insert(TRIPS_TABLE_NAME, null, contentValues);
        db.close();
    }

    public boolean updateTrip(TripDataItem trip){
        // TODO: update trip in database
        throw new java.lang.UnsupportedOperationException("Method not yet implemented.");
    }

    public TripDataItem getTrip(Date date){
        // TODO: Get specific trip from database
        throw new java.lang.UnsupportedOperationException("Method not yet implemented.");
    }

    public ArrayList<TripDataItem> getAllTrips(){
        ArrayList<TripDataItem> trips = new ArrayList<TripDataItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery ("SELECT * FROM " + TRIPS_TABLE_NAME, null);

        // Read data off of cursor
        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    Date date = new Date(cursor.getLong(cursor.getColumnIndex(TRIPS_COLUMN_DATE)));
                    double gallons = cursor.getDouble(cursor.getColumnIndex(TRIPS_COLUMN_GALLONS));
                    double miles = cursor.getDouble(cursor.getColumnIndex(TRIPS_COLUMN_MILES));
                    double cost = cursor.getDouble(cursor.getColumnIndex(TRIPS_COLUMN_COST));
                    trips.add(new TripDataItem(date, gallons, miles, cost));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        db.close();;
        return trips;
    }
}
