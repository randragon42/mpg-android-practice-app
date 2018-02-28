package com.mpgtracker.fragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mpgtracker.data.Trip;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;


public class BaseGraphFragment extends Fragment {
    protected void setUpGraph(GraphView graph, LineGraphSeries<DataPoint> series, double yAxisGranularity, String seriesTitle, String yValToastStringFormat){

        double minY = Math.floor(series.getLowestValueY() / yAxisGranularity) * yAxisGranularity;
        double maxY = Math.ceil(series.getHighestValueY() /yAxisGranularity) * yAxisGranularity;
        int numVerticalLabels = (int) ((maxY - minY) / yAxisGranularity) + 1;
        addSeriesToGraph(graph, series, seriesTitle, yValToastStringFormat);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
        graph.getGridLabelRenderer().setNumVerticalLabels(numVerticalLabels);

        // TODO: allow user to specify date range to display on graph
        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(series.getLowestValueX());
        graph.getViewport().setMaxX(series.getHighestValueX());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(minY);
        graph.getViewport().setMaxY(maxY);
        graph.getViewport().setYAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

    }

    protected void addSeriesToGraph(GraphView graph, LineGraphSeries<DataPoint> series, String seriesTitle, final String yValToastStringFormat){
        series.setTitle(seriesTitle);
        series.setDrawDataPoints(true);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                String yVal = String.format(yValToastStringFormat, dataPoint.getY());
                String date = Trip.DATE_FORMAT.format(dataPoint.getX());
                Toast.makeText(getActivity(), yVal + " - " + date , Toast.LENGTH_SHORT).show();
            }
        });
        graph.addSeries(series);
    }
}
