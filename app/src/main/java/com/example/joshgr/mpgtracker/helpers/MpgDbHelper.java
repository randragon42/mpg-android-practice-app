package com.example.joshgr.mpgtracker.helpers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.joshgr.mpgtracker.data.TripDataItem;

import java.util.ArrayList;
import java.util.Date;

//TODO: Update to new Rooms Persistence Library
public class MpgDbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "MPG_SQLite.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TRIPS_TABLE_NAME = "trips";
    public static final String TRIPS_COLUMN_ID = "_id";
    public static final String TRIPS_COLUMN_DATE = "date";
    public static final String TRIPS_COLUMN_GALLONS = "gallons";
    public static final String TRIPS_COLUMN_MILES = "miles";
    public static final String TRIPS_COLUMN_COST = "cost";
    public static final String TRIPS_COLUMN_ODOMETER = "odometer";
    public static final String TRIPS_COLUMN_FILLED_TANK = "filled_tank";

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
            TRIPS_COLUMN_ODOMETER + " REAL," +
            TRIPS_COLUMN_FILLED_TANK + " REAL," +
            TRIPS_COLUMN_COST + " REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TRIPS_TABLE_NAME);
        onCreate(db);
    }

    public int addTrip(TripDataItem trip){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValues(trip);
        long rowId = db.insert(TRIPS_TABLE_NAME, null, contentValues);
        db.close();
        return (int)rowId;
    }

    public void updateTrip(TripDataItem trip, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = createContentValues(trip);
        db.update(TRIPS_TABLE_NAME, contentValues, TRIPS_COLUMN_ID+"="+id, null);
        db.close();
    }

    public TripDataItem getTrip(int id){
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
                    int id = cursor.getInt(cursor.getColumnIndex(TRIPS_COLUMN_ID));
                    String date = cursor.getString(cursor.getColumnIndex(TRIPS_COLUMN_DATE));
                    double gallons = cursor.getDouble(cursor.getColumnIndex(TRIPS_COLUMN_GALLONS));
                    double miles = cursor.getDouble(cursor.getColumnIndex(TRIPS_COLUMN_MILES));
                    double cost = cursor.getDouble(cursor.getColumnIndex(TRIPS_COLUMN_COST));
                    double odometer = cursor.getDouble(cursor.getColumnIndex(TRIPS_COLUMN_ODOMETER));
                    boolean filledTank = cursor.getInt(cursor.getColumnIndex(TRIPS_COLUMN_FILLED_TANK)) == 1;
                    trips.add(new TripDataItem(id, date, gallons, miles, cost, odometer, filledTank));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        db.close();
        return trips;
    }

    public void deleteTrip(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TRIPS_TABLE_NAME, TRIPS_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        db.close();
    }

    public void deleteAllTrips(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TRIPS_TABLE_NAME, null, null);
        db.close();
    }

    private ContentValues createContentValues(TripDataItem trip){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRIPS_COLUMN_DATE, trip.getDate());
        contentValues.put(TRIPS_COLUMN_GALLONS, trip.getGallons());
        contentValues.put(TRIPS_COLUMN_MILES, trip.getMiles());
        contentValues.put(TRIPS_COLUMN_COST, trip.getTripCost());
        contentValues.put(TRIPS_COLUMN_ODOMETER, trip.getOdometer());
        contentValues.put(TRIPS_COLUMN_FILLED_TANK, trip.getFilledTank() ? 1 : 0);

        return contentValues;
    }
}
