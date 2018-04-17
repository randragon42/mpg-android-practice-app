package com.mpgtracker.helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.mpgtracker.R;

public class UnitHelper {

    public final String IMPERIAL;
    public final String METRIC;
    public final String UNITS;
    private Context mContext;

    public UnitHelper(Context context) {
        IMPERIAL = context.getResources().getString(R.string.imperial);
        METRIC = context.getResources().getString(R.string.metric);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        UNITS = prefs.getString("pref_units", null);

        mContext = context;
    }

    public boolean isMetric() {
        if(UNITS != null && METRIC.equals(UNITS)){
            return true;
        }
        return false;
    }

    public String getEfficiencyLabel() {
        if(isMetric()){
            return mContext.getResources().getString(R.string.km_per_l);
        }
        return mContext.getResources().getString(R.string.mpg);
    }

    public String getVolumeLabel() {
        if(isMetric()){
            return mContext.getResources().getString(R.string.l);
        }
        return mContext.getResources().getString(R.string.gal);
    }

    public String getDistanceLabel() {
        if(isMetric()){
            return mContext.getResources().getString(R.string.km);
        }
        return mContext.getResources().getString(R.string.miles);
    }

    public String getEfficiencyTitle() {
        if(isMetric()){
            return mContext.getResources().getString(R.string.KM_per_L);
        }
        return mContext.getResources().getString(R.string.MPG);
    }

    public String getVolumeTitle() {
        if(isMetric()){
            return mContext.getResources().getString(R.string.Liters);
        }
        return mContext.getResources().getString(R.string.Gallons);
    }



}
