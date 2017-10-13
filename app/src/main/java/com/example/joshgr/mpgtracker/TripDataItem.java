package com.example.joshgr.mpgtracker;

import java.util.Date;

public class TripDataItem {

    Date mDate;
    double mGallons;
    double mMiles;
    double mTripCost;
    double mCostPerGallon;
    double mMilesPerGallon;


    public TripDataItem(Date date, double gallons, double miles, double tripCost){
        mDate = date;
        mGallons = gallons;
        mMiles = miles;
        mTripCost = tripCost;
        mCostPerGallon = tripCost / gallons;
        mMilesPerGallon = miles / gallons;
    }

    public Date getDate(){
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


}
