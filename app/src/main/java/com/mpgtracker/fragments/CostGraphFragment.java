package com.mpgtracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpgtracker.R;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.trips.Trip;

import java.util.List;


public class CostGraphFragment extends BaseGraphFragment {

    List<Trip> mTripList;
    private VehicleViewModel mVehicleViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);
        mTripList = mVehicleViewModel.getAllTrips().getValue();

        // Set up Graph

    }

}
