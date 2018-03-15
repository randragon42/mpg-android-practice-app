package com.mpgtracker.fragments;


public class MpgGraphFragment extends BaseGraphFragment {

    public MpgGraphFragment() {
        String dataFormat = "%.2f mpg";

        super.setChartType(dataFormat);
    }

}
