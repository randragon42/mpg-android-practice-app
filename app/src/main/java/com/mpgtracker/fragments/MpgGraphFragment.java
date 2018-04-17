package com.mpgtracker.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mpgtracker.helpers.UnitHelper;

public class MpgGraphFragment extends BaseGraphFragment {

    public MpgGraphFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        UnitHelper unitHelper = new UnitHelper(getActivity().getApplicationContext());
        String dataFormat = "%.2f " + unitHelper.getEfficiencyLabel();

        super.setChartType(dataFormat);
        super.onViewCreated(view, savedInstanceState);
    }

}
