package com.mpgtracker.fragments;

public class DistanceGraphFragment extends BaseGraphFragment {

    public DistanceGraphFragment() {
        String dataFormat = "%.1f miles";

        super.setChartType(dataFormat);
    }
}
