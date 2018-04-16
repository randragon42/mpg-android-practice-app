package com.mpgtracker.fragments;

public class DistanceGraphFragment extends BaseGraphFragment {

    public DistanceGraphFragment() {
        String dataFormat = "%.1f distance";

        super.setChartType(dataFormat);
    }
}
