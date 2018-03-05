package com.mpgtracker.data.trips;


public class TripStat {
    private String mStatName;
    private String mStatValue;

    public TripStat(String statName, String statValue){
        mStatName = statName;
        mStatValue = statValue;
    }

    public String getStatName(){return mStatName;}
    public String getStatValue() {return mStatValue;}
    public void setStatValue(String value) {mStatValue = value;}
}
