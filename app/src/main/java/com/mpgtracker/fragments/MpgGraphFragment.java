package com.mpgtracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.AndroidResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mpgtracker.R;
import com.mpgtracker.data.MPAndroidChart.XAxisDateFormatter;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.trips.Trip;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class MpgGraphFragment extends BaseGraphFragment {

    List<Trip> mTripList;
    private VehicleViewModel mVehicleViewModel;
    private LineChart mChart;
    private XAxis mXAxis;
    private List<Button> mIntervalButtons;

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

        if(mTripList == null || mTripList.isEmpty()){
            LinearLayout intervalButtons = view.findViewById(R.id.interval_button_layout);
            intervalButtons.setVisibility(View.GONE);
        }
        else {
            // Set up buttons
            mIntervalButtons = new ArrayList<>();
            Button oneMonth = view.findViewById(R.id.one_month);
            Button threeMonths = view.findViewById(R.id.three_months);
            Button oneYear = view.findViewById(R.id.one_year);
            Button allTime = view.findViewById(R.id.all_time);

            mIntervalButtons.add(oneMonth);
            mIntervalButtons.add(threeMonths);
            mIntervalButtons.add(oneYear);
            mIntervalButtons.add(allTime);

            oneMonth.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    updateXAxis(30);
                    selectButton((Button)v);
                }
            });
            threeMonths.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    updateXAxis(90);
                    selectButton((Button)v);
                }
            });
            oneYear.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    updateXAxis(365);
                    selectButton((Button)v);
                }
            });
            allTime.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    updateXAxis(-1);
                    selectButton((Button)v);
                }
            });

            // Set up Chart
            mChart = view.findViewById(R.id.chart);
            createChart();
        }

    }

    private void selectButton(Button selectedButton) {
        for(Button button : mIntervalButtons){
            button.setTextColor(getResources().getColor(R.color.grey));
            button.setBackground(null);
        }
        selectedButton.setBackground(getResources().getDrawable(R.drawable.button_bottom_selected));
        selectedButton.setTextColor(getResources().getColor(R.color.white));
    }

    private void createChart() {

        //chart.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mChart.setScaleEnabled(false);
        mChart.setExtraOffsets(10f, 0f, 10f, 20f);
        List<Entry> entries = createChartData();

        LineDataSet dataSet = new LineDataSet(entries, "MPG"); // add entries to dataset

        // Style the dataset
        dataSet.setColor(getResources().getColor(R.color.white));
        dataSet.setLineWidth(5f);
        dataSet.setCircleRadius(6f);
        dataSet.setCircleHoleRadius(6f);
        dataSet.setCircleColor(getResources().getColor(R.color.white));
        dataSet.setCircleColorHole(getResources().getColor(R.color.white));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setDrawValues(false);

        // Set up X-Axis
        mXAxis = mChart.getXAxis();
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1f); // restrict interval to 1 (minimum)
//        xAxis.setGranularityEnabled(true);
        mXAxis.setAxisMinimum(getDay(-30).getTime());
        mXAxis.setAxisMaximum(getDay(3).getTime());
        mXAxis.setTextSize(15f);
        mXAxis.setTextColor(getResources().getColor(R.color.white));
        mXAxis.setDrawAxisLine(false);
        mXAxis.setDrawGridLines(false);
        mXAxis.setLabelCount(3);
        mXAxis.setValueFormatter(new XAxisDateFormatter());

        // Set up left Y-Axis
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextSize(15f);
        leftAxis.setTextColor(getResources().getColor(R.color.white));
//        leftAxis.setAxisLineColor();
        leftAxis.setDrawAxisLine(false);
//        leftAxis.setDrawGridLines(false);

        // Hide things we don't want
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
        mChart.setNoDataText("You have no trips for this vehicle. Add a trip to see charts.");
        mChart.setNoDataTextColor(getResources().getColor(R.color.white));
        mChart.getDescription().setEnabled(false);

        // Refresh the chart
        mChart.invalidate(); // refresh
    }

    private List<Entry> createChartData() {
        List<Entry> entries = new ArrayList<Entry>();
        for(Trip trip : mTripList) {
            entries.add(new Entry(trip.getDate().getTime(), (float)trip.getMilesPerGallon()));
        }
        return entries;
    }

    private void updateXAxis(int interval) {
        if(interval == -1){
            // Fit all records on graph
            LocalDate earliestDate = new LocalDate(mTripList.get(0).date);
            int offset = Days.daysBetween(earliestDate, LocalDate.now()).getDays();
            int padding = offset/6;
            mXAxis.setAxisMinimum(getDay(-(offset + padding)).getTime());
            mXAxis.setAxisMaximum(getDay(padding).getTime());
        }
        else {
            int padding = interval / 6;
            mXAxis.setAxisMinimum(getDay(-(interval + padding)).getTime());
            mXAxis.setAxisMaximum(getDay(padding).getTime());
        }
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    private Date getDay(int offset){
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, offset);
        return cal.getTime();
    }

}
