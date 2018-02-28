package com.mpgtracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpgtracker.R;
import com.mpgtracker.data.Trip;
import com.mpgtracker.data.TripViewModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;


public class CostGraphFragment extends BaseGraphFragment {

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
        // Get ViewModel
        mTripViewModel = ViewModelProviders.of(getActivity()).get(TripViewModel.class);
        mTripList = mTripViewModel.getAllTrips().getValue();

        TextView titleText = (TextView) view.findViewById(R.id.graph_title);
        titleText.setText(getResources().getString(R.string.cost_over_time));

        // Set up Graph
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = getGraphData();
        setUpGraph(graph, series, 5, "Cost Over Time", "$%.2f");
    }

    private LineGraphSeries<DataPoint> getGraphData(){
        DataPoint[] dataPoints = new DataPoint[mTripList.size()];

        for(int i=0; i<mTripList.size(); i++){
            Trip trip = mTripList.get(i);
            double tripCost = trip.getTripCost();
            dataPoints[i] = new DataPoint(trip.date, tripCost);
        }

        return new LineGraphSeries<>(dataPoints);
    }
}
