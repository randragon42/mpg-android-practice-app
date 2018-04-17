package com.mpgtracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mpgtracker.helpers.UnitHelper;

public class DistanceGraphFragment extends BaseGraphFragment {

    public DistanceGraphFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        UnitHelper unitHelper = new UnitHelper(getActivity());
        String dataFormat = "%.1f " + unitHelper.getDistanceLabel();

        super.setChartType(dataFormat);
        super.onViewCreated(view, savedInstanceState);
    }
}
