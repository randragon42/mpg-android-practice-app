package com.example.joshgr.mpgtracker.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joshgr.mpgtracker.R;
import com.example.joshgr.mpgtracker.adapters.StatArrayAdapter;
import com.example.joshgr.mpgtracker.data.TripEntity;
import com.example.joshgr.mpgtracker.data.TripStat;
import com.example.joshgr.mpgtracker.data.TripsDatabase;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends BaseFragment {
    private List<TripEntity> mTripList;
    private StatArrayAdapter mAdapter;
    private List<TripStat> mStatsList;

    @Override
    protected String getTitle() { return getResources().getString(R.string.stats_title); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TripsDatabase db = TripsDatabase.getTripsDatabase(getContext());
        mTripList = db.tripDAO().getAll();
        mStatsList = compileAllStats();

        // Get average MPG
        double avgMpg = getAverageMpg();
        TextView averageMpg = (TextView) view.findViewById(R.id.average_mpg);
        averageMpg.setText(String.format("%.2f", avgMpg));

        // Set up list adapter
        ListView statsListView = (ListView) view.findViewById(R.id.stats_list_view);
        mAdapter = new StatArrayAdapter(view.getContext(), R.layout.stat_item, mStatsList);
        statsListView.setAdapter(mAdapter);
    }

    private List<TripStat> compileAllStats(){
        List<TripStat> tripStats = new ArrayList<>();

        double avgCost;
        double avgMiles;
        double avgGallons;
        double totalCost = 0;
        double totalMiles = 0;
        double totalGallons = 0;

        for(int i=0; i<mTripList.size(); i++){
            TripEntity trip = mTripList.get(i);
            totalCost = totalCost + trip.getTripCost();
            totalMiles = totalMiles + trip.getMiles();
            totalGallons = totalGallons + trip.getGallons();
        }

        avgCost = totalCost/mTripList.size();
        avgMiles = totalMiles/mTripList.size();
        avgGallons = totalGallons/mTripList.size();

        tripStats.add(new TripStat(getResources().getString(R.string.average_cost), String.format("$%.2f", avgCost)));
        tripStats.add(new TripStat(getResources().getString(R.string.average_miles), String.format("%.1f", avgMiles)));
        tripStats.add(new TripStat(getResources().getString(R.string.average_gallons), String.format("%.3f", avgGallons)));
        tripStats.add(new TripStat(getResources().getString(R.string.total_cost), String.format("$%.2f", totalCost)));
        tripStats.add(new TripStat(getResources().getString(R.string.total_miles), String.format("%.1f", totalMiles)));
        tripStats.add(new TripStat(getResources().getString(R.string.total_gallons), String.format("%.3f", totalGallons)));

        return tripStats;
    }

    private double getAverageMpg(){
        double avgMpg = 0;
        for(int i=0; i<mTripList.size(); i++){
            avgMpg = avgMpg + mTripList.get(i).getMilesPerGallon();
        }

        return avgMpg / mTripList.size();
    }
}
