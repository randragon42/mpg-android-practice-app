package com.mpgtracker.data.MPAndroidChart;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XAxisDateFormatter implements IAxisValueFormatter {


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d");

            return sdf.format(new Date((long)value));
        }
        catch (Exception e)
        {
            return  "";
        }
    }

}