package com.mpgtracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mpgtracker.fragments.CostGraphFragment;
import com.mpgtracker.fragments.DistanceGraphFragment;
import com.mpgtracker.fragments.MpgGraphFragment;


public class GraphPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public GraphPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MpgGraphFragment mpgGraphFragment = new MpgGraphFragment();
                return mpgGraphFragment;
            case 1:
                DistanceGraphFragment distanceGraphFragment = new DistanceGraphFragment();
                return distanceGraphFragment;
            case 2:
                CostGraphFragment costGraphFragment = new CostGraphFragment();
                return costGraphFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
