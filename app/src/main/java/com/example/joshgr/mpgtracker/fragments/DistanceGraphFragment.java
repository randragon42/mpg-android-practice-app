package com.example.joshgr.mpgtracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joshgr.mpgtracker.R;
import com.example.joshgr.mpgtracker.data.Trip;
import com.example.joshgr.mpgtracker.data.TripViewModel;
import com.example.joshgr.mpgtracker.data.TripsDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.util.Collections;
import java.util.List;


public class DistanceGraphFragment extends BaseGraphFragment {

    List<Trip> mTripList;
    private TripViewModel mTripViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TripsDatabase db = TripsDatabase.getTripsDatabase(getContext());
        //mTripList = db.tripDAO().getAllTrips();
        //Collections.sort(mTripList);
        // Get ViewModel
        mTripViewModel = ViewModelProviders.of(getActivity()).get(TripViewModel.class);
        mTripList = mTripViewModel.getAllTrips().getValue();

        TextView titleText = (TextView) view.findViewById(R.id.graph_title);
        titleText.setText(getResources().getString(R.string.distance_over_time));

        // Set up Graph
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
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
