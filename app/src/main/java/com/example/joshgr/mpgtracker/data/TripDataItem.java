package com.example.joshgr.mpgtracker.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TripDataItem {

    int mId;
    String mDate;
    double mGallons;
    double mMiles;
    double mTripCost;
    double mCostPerGallon;
    double mMilesPerGallon;
    double mOdometer;
    boolean mFilledTank;

    public TripDataItem(int id, String date, double gallons, double miles, double tripCost, double odometer, boolean filledTank){
        mId = id;
        mDate = date;
        mGallons = gallons;
        mMiles = miles;
        mTripCost = tripCost;
        mCostPerGallon = tripCost / gallons;
        mMilesPerGallon = miles / gallons;
        mOdometer = odometer;
        mFilledTank = filledTank;
    }

    public int getId() {
        return mId;
    }

    public String getDate(){
        return mDate;
    }

    public double getGallons(){
        return mGallons;
    }

    public double getMiles(){
        return mMiles;
    }

    public double getTripCost(){
        return mTripCost;
    }

    public double getCostPerGallon(){
        return mCostPerGallon;
    }

    public double getMilesPerGallon(){
        return mMilesPerGallon;
    }

    public double getOdometer(){
        return mOdometer;
    }

    public boolean getFilledTank(){
        return mFilledTank;
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

}
