package com.mpgtracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpgtracker.R;
import com.mpgtracker.data.trips.Trip;
import com.mpgtracker.data.VehicleViewModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.util.List;


public class DistanceGraphFragment extends BaseGraphFragment {

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

        TextView titleText = view.findViewById(R.id.graph_title);
        titleText.setText(getResources().getString(R.string.distance_over_time));

        // Set up Graph
        GraphView graph = view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = getGraphData();
        setUpGraph(graph, series, 25, "Distance Over Time", "%.1f");
    }

    private LineGraphSeries<DataPoint> getGraphData(){
        DataPoint[] distanceDataPoints = new DataPoint[mTripList.size()];

        for(int i=0; i<mTripList.size(); i++){
            Trip trip = mTripList.get(i);
            double tripDistance = trip.getMiles();

            distanceDataPoints[i] = new DataPoint(trip.date, tripDistance);
        }

        return new LineGraphSeries<>(distanceDataPoints);
    }
}
