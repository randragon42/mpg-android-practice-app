package com.mpgtracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mpgtracker.R;
import com.mpgtracker.data.MPAndroidChart.MpgMarkerView;
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


public class BaseGraphFragment extends Fragment {
    List<Trip> mTripList;
    private VehicleViewModel mVehicleViewModel;
    private LineChart mChart;
    private XAxis mXAxis;
    private List<Button> mIntervalButtons;

    // Variables to customize chart type
    private String mDataFormat;

    public void setChartType(String dataFormat) {
        mDataFormat = dataFormat;
    }

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
        mChart = view.findViewById(R.id.chart);

        if(mTripList == null || mTripList.isEmpty()){
            LinearLayout intervalButtons = view.findViewById(R.id.interval_button_layout);
            intervalButtons.setVisibility(View.GONE);
            setEmptyChart();
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

    private void setEmptyChart() {
        mChart.setNoDataText("You have no trips for this vehicle. Add a trip to see charts.");
        Paint paint = mChart.getPaint(Chart.PAINT_INFO);
        paint.setTextSize(50);
        paint.setColor(getResources().getColor(R.color.white));
    }

    private void createChart() {

        mChart.setScaleEnabled(false);  // disable zoom
        mChart.setExtraOffsets(10f, 0f, 10f, 20f);  // set padding around chart
        List<Entry> entries = createChartData();
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
        updateXAxis(30);
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

        // Set up Tooltip (MarkerViews)
        MpgMarkerView mv = new MpgMarkerView(getContext(), R.layout.marker_view, mDataFormat);
        mChart.setMarker(mv);
        mChart.setMaxHighlightDistance(20f);    // Set distance in dp to register datapoint selection

        // Hide things we don't want
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
        mChart.getDescription().setEnabled(false);

        // Refresh the chart
        mChart.invalidate(); // refresh
    }

    private List<Entry> createChartData() {
        List<Entry> entries = new ArrayList<Entry>();
        for(Trip trip : mTripList) {
            if(this instanceof MpgGraphFragment){
                entries.add(new Entry(trip.getDate().getTime(), (float)trip.getMilesPerGallon()));
            }
            else if(this instanceof CostGraphFragment){
                entries.add(new Entry(trip.getDate().getTime(), (float)trip.getTripCost()));
            }
            else if(this instanceof DistanceGraphFragment){
                entries.add(new Entry(trip.getDate().getTime(), (float)trip.getMiles()));
            }
        }
        return entries;
    }

    private void updateXAxis(int interval) {
        if(interval == -1){
            // Fit all records on graph
            LocalDate earliestDate = new LocalDate(mTripList.get(0).date);
            int offset = Days.daysBetween(earliestDate, LocalDate.now()).getDays();
            int padding = offset/6;
            mXAxis.setAxisMinimum(getDay(-(offset + padding + 1)).getTime());
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
