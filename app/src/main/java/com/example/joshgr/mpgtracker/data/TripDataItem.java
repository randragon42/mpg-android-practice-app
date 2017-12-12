package com.example.joshgr.mpgtracker.data;

import java.util.Date;

public class TripDataItem {

    int mId;
    Date mDate;
    double mGallons;
    double mMiles;
    double mTripCost;
    double mCostPerGallon;
    double mMilesPerGallon;

    public TripDataItem(int id, Date date, double gallons, double miles, double tripCost){
        mId = id;
        mDate = date;
        mGallons = gallons;
        mMiles = miles;
        mTripCost = tripCost;
        mCostPerGallon = tripCost / gallons;
        mMilesPerGallon = miles / gallons;
    }

    public int getId() { return mId; }

    public Date getDate(){ return mDate; }

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


}
