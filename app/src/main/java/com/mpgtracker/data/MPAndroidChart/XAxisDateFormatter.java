package com.mpgtracker.data.MPAndroidChart;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class XAxisDateFormatter implements IAxisValueFormatter {

    private String mDateFormat;

    public XAxisDateFormatter(String dateFormat) {
        mDateFormat = dateFormat;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(mDateFormat);

            return sdf.format(new Date((long)value));
        }
        catch (Exception e)
        {
            return  "";
        }
    }

}