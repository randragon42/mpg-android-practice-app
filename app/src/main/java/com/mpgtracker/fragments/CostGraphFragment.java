package com.mpgtracker.fragments;

public class CostGraphFragment extends BaseGraphFragment {

    public CostGraphFragment() {
        String dataFormat = "$%.2f";

        super.setChartType(dataFormat);
    }

}
