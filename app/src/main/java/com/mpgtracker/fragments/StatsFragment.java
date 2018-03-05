package com.mpgtracker.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpgtracker.R;
import com.mpgtracker.data.trips.TripStat;
import com.mpgtracker.data.trips.TripStats;
import com.mpgtracker.data.VehicleViewModel;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends BaseFragment {
    private List<TripStat> mStatsList;
    private VehicleViewModel mVehicleViewModel;
    private TripStats mTripStats;
    private boolean mNoTripData = false;
    private boolean mNoMpgData = false;

    @Override
    protected String getTitle() { return getResources().getString(R.string.stats_title); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTripStats = mVehicleViewModel.getTripStats();
        mNoTripData = mVehicleViewModel.getAllTrips().getValue().size() == 0;
        mNoMpgData = mTripStats.getAverageMpg() == 0;
        mStatsList = compileAllStats();

        TextView averageMpg = view.findViewById(R.id.average_mpg);
        TextView averageMpgLabel = view.findViewById(R.id.average_mpg_label);
        if(mNoTripData){
            averageMpg.setText("No Trip Data");
            averageMpgLabel.setText("");
        }
        else if(mNoMpgData){
            averageMpg.setText("No MPG Data");
            averageMpgLabel.setText("");
        }
        else {
            // Get average MPG
            double avgMpg = mTripStats.getAverageMpg();
            averageMpg.setText(String.format("%.2f", avgMpg));
        }

        // Set up mpg linear layout
        setLinearLayout(view, mStatsList.subList(0,3), R.id.mpg_stats_list_view);

        // Set up distance linear layout
        setLinearLayout(view, mStatsList.subList(3,7), R.id.distance_stats_list_view);

        // Set up cost linear layout
        setLinearLayout(view, mStatsList.subList(7,11), R.id.cost_stats_list_view);

        // Set up gallons linear layout
        setLinearLayout(view, mStatsList.subList(11, mStatsList.size()), R.id.gallons_stats_list_view);
    }

    private void setLinearLayout(View view, List<TripStat> stats, int linearLayoutId) {
        LinearLayout linearLayout = view.findViewById(linearLayoutId);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (TripStat stat:stats) {
            View rowView  = inflater.inflate(R.layout.stat_item, linearLayout, false);
            ((TextView)rowView.findViewById(R.id.stat_name)).setText(stat.getStatName());
            ((TextView)rowView.findViewById(R.id.stat_value)).setText(stat.getStatValue());
            linearLayout.addView(rowView);
        }

    }

    private List<TripStat> compileAllStats(){
        List<TripStat> tripStats = new ArrayList<>();

        // MPG Stats (0, 2)
        tripStats.add(new TripStat(getResources().getString(R.string.average_mpg), String.format("%.2f", mTripStats.getAverageMpg())));
        tripStats.add(new TripStat(getResources().getString(R.string.best_mpg), String.format("%.2f", mTripStats.getBestMpg())));
        tripStats.add(new TripStat(getResources().getString(R.string.worst_mpg), String.format("%.2f", mTripStats.getWorstMpg())));
        if (mNoMpgData) {
            for(TripStat stat: tripStats){
                stat.setStatValue("-");
            }
        }

        // Distance Stats (3, 6)
        tripStats.add(new TripStat(getResources().getString(R.string.average_distance), String.format("%.1f", mTripStats.getAverageMiles())));
        tripStats.add(new TripStat(getResources().getString(R.string.longest_trip), String.format("%.1f", mTripStats.getMostMiles())));
        tripStats.add(new TripStat(getResources().getString(R.string.shortest_trip), String.format("%.1f", mTripStats.getLeastMiles())));
        tripStats.add(new TripStat(getResources().getString(R.string.total_distance), String.format("%.1f", mTripStats.getTotalMiles())));

        // Cost Stats (7, 10)
        tripStats.add(new TripStat(getResources().getString(R.string.average_cost), String.format("$%.2f", mTripStats.getAverageCost())));
        tripStats.add(new TripStat(getResources().getString(R.string.highest_cost), String.format("$%.2f", mTripStats.getHighestCost())));
        tripStats.add(new TripStat(getResources().getString(R.string.lowest_cost), String.format("$%.2f", mTripStats.getLowestCost())));
        tripStats.add(new TripStat(getResources().getString(R.string.total_cost), String.format("$%.2f", mTripStats.getTotalCost())));

        // Gallons Stats (11, 14)
        tripStats.add(new TripStat(getResources().getString(R.string.average_gallons), String.format("%.3f", mTripStats.getAverageGallons())));
        tripStats.add(new TripStat(getResources().getString(R.string.most_gallons), String.format("%.3f", mTripStats.getMostGallons())));
        tripStats.add(new TripStat(getResources().getString(R.string.least_gallons), String.format("%.3f", mTripStats.getLeastGallons())));
        tripStats.add(new TripStat(getResources().getString(R.string.total_gallons), String.format("%.3f", mTripStats.getTotalGallons())));

        if(mNoTripData) {
            for(TripStat stat: tripStats){
                stat.setStatValue("-");
            }
        }

        return tripStats;
    }
}
