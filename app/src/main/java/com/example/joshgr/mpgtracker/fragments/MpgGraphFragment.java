package com.example.joshgr.mpgtracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.joshgr.mpgtracker.R;
import com.example.joshgr.mpgtracker.data.TripEntity;
import com.example.joshgr.mpgtracker.data.TripsDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.List;


public class MpgGraphFragment extends Fragment {
    List<TripEntity> mTripList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mpg_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TripsDatabase db = TripsDatabase.getTripsDatabase(getContext());
        mTripList = db.tripDAO().getAll();

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = getGraphData();
        series.setTitle("MPG Over Time");
        series.setDrawDataPoints(true);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "MPG: "+ String.format("%.2f", dataPoint.getY()), Toast.LENGTH_SHORT).show();
            }
        });
        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // TODO: allow user to specify date range to display on graph
        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(series.getLowestValueX());
        graph.getViewport().setMaxX(series.getHighestValueX());
        graph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    private LineGraphSeries<DataPoint> getGraphData(){
        DataPoint[] dataPoints = new DataPoint[mTripList.size()];

        for(int i=0; i<mTripList.size(); i++){
            TripEntity trip = mTripList.get(i);
            dataPoints[i] = new DataPoint(trip.date, trip.getMilesPerGallon());
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

        return series;
    }
}
