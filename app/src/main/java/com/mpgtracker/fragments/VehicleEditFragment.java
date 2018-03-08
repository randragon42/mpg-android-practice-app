package com.mpgtracker.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpgtracker.R;
import com.mpgtracker.data.VehicleViewModel;

public class VehicleEditFragment extends BaseFragment {

    private boolean mCreateMode;
    private VehicleViewModel mVehicleViewModel;

    @Override
    protected String getTitle() {
        if(mCreateMode){ return getResources().getString(R.string.create_vehicle_title); }
        else { return getResources().getString(R.string.edit_vehicle_title); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_edit, container, false);

        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mVehicleViewModel.getSelectedVehicle().getValue() == null) {
            mCreateMode = true;
        }
        super.onViewCreated(view, savedInstanceState);


    }
}
