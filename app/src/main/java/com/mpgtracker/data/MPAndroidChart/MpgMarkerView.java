package com.mpgtracker.data.MPAndroidChart;


import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.mpgtracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MpgMarkerView extends MarkerView {

    private TextView mDateTextView;
    private TextView mDataTextView;
    private String mDataFormat;

    public MpgMarkerView (Context context, int layoutResource, String dataFormat) {
        super(context, layoutResource);
        mDateTextView = findViewById(R.id.marker_date);
        mDataTextView = findViewById(R.id.marker_data);
        mDataFormat = dataFormat;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, YYYY");
        String date = sdf.format(new Date((long)e.getX()));

        String data = String.format(mDataFormat, e.getY());
        mDateTextView.setText(date);
        mDataTextView.setText(data);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}