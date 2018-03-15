package com.mpgtracker.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mpgtracker.R;
import com.mpgtracker.adapters.GraphPageAdapter;
import com.mpgtracker.data.MPAndroidChart.MpgMarkerView;
import com.mpgtracker.data.MPAndroidChart.XAxisDateFormatter;
import com.mpgtracker.data.trips.Trip;
import com.mpgtracker.data.trips.TripStat;
import com.mpgtracker.data.trips.TripStats;
import com.mpgtracker.data.VehicleViewModel;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class StatsFragment extends BaseFragment {
    private List<TripStat> mStatsList;
    private List<Trip> mTripList;
    private VehicleViewModel mVehicleViewModel;
    private ActionBar mActionBar;
    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private TextView mHeader;
    private LineChart mChart;
    private XAxis mXAxis;

    private TripStats mTripStats;
    private boolean mNoTripData = false;
    private boolean mNoMpgData = false;

    @Override
    protected String getTitle() { return null; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat_chart, container, false);

        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);
        mTripList = mVehicleViewModel.getAllTrips().getValue();

        // Add spinner nav to toolbar
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mToolbar = getActivity().findViewById(R.id.toolbar);
        addSpinnerNavigationToActionBar();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTripStats = mVehicleViewModel.getTripStats();
        mNoTripData = mVehicleViewModel.getAllTrips().getValue().size() == 0;
        mNoMpgData = mTripStats.getAverageMpg() == 0;
        mStatsList = compileAllStats();

        mHeader = view.findViewById(R.id.stat_header);
        mLinearLayout = view.findViewById(R.id.stats_list);
        mChart = view.findViewById(R.id.chart);

    }

    @Override
    public void onDestroy() {
        Spinner spinner = mToolbar.findViewWithTag("spinner_nav");
        mToolbar.removeView(spinner);
        mActionBar.setDisplayShowTitleEnabled(true);

        super.onDestroy();
    }

    private void setLinearLayout(LinearLayout linearLayout, List<TripStat> stats) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        linearLayout.removeAllViews();
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

    private void addSpinnerNavigationToActionBar() {
        if(mToolbar.findViewWithTag("spinner_nav")==null) {
            Spinner spinner = new Spinner(getActivity());
            spinner.setTag("spinner_nav");
            // TODO: change spinner text color

            // Set chart options
            List<String> chartOptions = new ArrayList<>();
            chartOptions.add(getResources().getString(R.string.mpg));
            chartOptions.add(getResources().getString(R.string.cost));
            chartOptions.add(getResources().getString(R.string.distance));

            //Setting up the adapter
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, chartOptions);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(spinnerAdapter);

            if (spinner != null) {
                spinner.setVisibility(View.VISIBLE);
                spinner.setAdapter(spinnerAdapter);
            }
            spinnerAdapter.notifyDataSetChanged();
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    switch(position) {
                        case(0):
                            mpgSelected();
                            break;
                        case(1):
                            costSelected();
                            break;
                        case(2):
                            distanceSelected();
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            mActionBar.setDisplayShowTitleEnabled(false);
            mToolbar.addView(spinner);
        }
    }

    private void mpgSelected() {
        mHeader.setText(getResources().getString(R.string.mpg));
        setLinearLayout(mLinearLayout, mStatsList.subList(0,3));
        createChart(createChartData(0));
    }

    private void distanceSelected() {
        mHeader.setText(getResources().getString(R.string.distance));
        setLinearLayout(mLinearLayout, mStatsList.subList(3,7));
        createChart(createChartData(2));
    }

    private void costSelected() {
        mHeader.setText(getResources().getString(R.string.cost));
        setLinearLayout(mLinearLayout, mStatsList.subList(7,11));
        createChart(createChartData(1));
    }

    private List<Entry> createChartData(int selection) {
        List<Entry> entries = new ArrayList<Entry>();
        for(Trip trip : mTripList) {
            if(selection == 0){
                entries.add(new Entry(trip.getDate().getTime(), (float)trip.getMilesPerGallon()));
            }
            else if(selection == 1){
                entries.add(new Entry(trip.getDate().getTime(), (float)trip.getTripCost()));
            }
            else if(selection == 2){
                entries.add(new Entry(trip.getDate().getTime(), (float)trip.getMiles()));
            }
        }
        return entries;
    }

    private void createChart(List<Entry> entries) {

        mChart.setScaleEnabled(false);  // disable zoom
        mChart.setExtraOffsets(10f, 0f, 10f, 20f);  // set padding around chart
        LineDataSet dataSet = new LineDataSet(entries, ""); // add entries to dataset

        // Style the dataset
        dataSet.setColor(getResources().getColor(R.color.white));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(4f);
        dataSet.setCircleHoleRadius(4f);
        dataSet.setCircleColor(getResources().getColor(R.color.white));
        dataSet.setCircleColorHole(getResources().getColor(R.color.white));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setDrawValues(false);

        // Set up X-Axis
        mXAxis = mChart.getXAxis();
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        LocalDate earliestDate = new LocalDate(mTripList.get(0).date);
        int offset = Days.daysBetween(earliestDate, LocalDate.now()).getDays();
        int padding = offset/6;
        mXAxis.setAxisMinimum(getDay(-(offset + padding + 1)).getTime());
        mXAxis.setAxisMaximum(getDay(padding).getTime());
        mXAxis.setTextSize(15f);
        mXAxis.setTextColor(getResources().getColor(R.color.white));
        mXAxis.setDrawAxisLine(false);
        mXAxis.setDrawGridLines(false);
        mXAxis.setLabelCount(3);
        mXAxis.setValueFormatter(new XAxisDateFormatter("MMM d"));

        // Set up left Y-Axis
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextSize(15f);
        leftAxis.setTextColor(getResources().getColor(R.color.white));
        leftAxis.setDrawAxisLine(false);

        // Hide things we don't want
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
        mChart.getDescription().setEnabled(false);

        // Refresh the chart
        mChart.invalidate(); // refresh
    }

    private Date getDay(int offset){
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, offset);
        return cal.getTime();
    }

}
