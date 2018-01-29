package com.example.joshgr.mpgtracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.joshgr.mpgtracker.R;
import com.example.joshgr.mpgtracker.data.TripDataItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TripArrayAdapter extends ArrayAdapter<TripDataItem> {
    private ArrayList<TripDataItem> mTrips;
    private Context mContext;

    public TripArrayAdapter(Context context, int listItemLayoutId, ArrayList<TripDataItem> trips){
        super(context, listItemLayoutId, trips);
        mContext = context;
        mTrips = trips;
    }

    @Nullable
    @Override
    public TripDataItem getItem(int position) {
        return mTrips.get(position);
    }

    @Override
    public int getCount() {
        return mTrips.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.trip_item, parent, false);

            TextView date = (TextView) view.findViewById(R.id.date);
            TextView year = (TextView) view.findViewById(R.id.year);
            TextView mpg = (TextView) view.findViewById(R.id.mpg);
            TextView cost = (TextView) view.findViewById(R.id.cost);
            TextView gallons = (TextView) view.findViewById(R.id.gallons);
            TextView costPerGallon = (TextView) view.findViewById(R.id.costPerGallon);
            TextView miles = (TextView) view.findViewById(R.id.miles);
            TextView odometer = (TextView) view.findViewById(R.id.odometer);

            TripViewHolder tripViewHolder = new TripViewHolder();
            tripViewHolder.Date = date;
            tripViewHolder.Year = year;
            tripViewHolder.MPG = mpg;
            tripViewHolder.Cost = cost;
            tripViewHolder.Gallons = gallons;
            tripViewHolder.CostPerGallon = costPerGallon;
            tripViewHolder.Miles = miles;
            tripViewHolder.Odometer = odometer;
            view.setTag(tripViewHolder);
        }

        TripViewHolder tripViewHolder = (TripViewHolder)view.getTag();

        // TODO: Add locale to formatting
        TripDataItem trip = mTrips.get(position);
        tripViewHolder.Date.setText(formatDate(trip.getDate(), "MMM dd"));
        tripViewHolder.Year.setText(formatDate(trip.getDate(), "yyyy"));
        tripViewHolder.MPG.setText(String.format("%.2f mpg", mTrips.get(position).getMilesPerGallon()));
        tripViewHolder.Cost.setText(String.format("$%.2f", mTrips.get(position).getTripCost()));
        tripViewHolder.Gallons.setText(String.format("%.3f gal", mTrips.get(position).getGallons()));
        tripViewHolder.CostPerGallon.setText(String.format("$%.3f/gal", mTrips.get(position).getCostPerGallon()));
        tripViewHolder.Miles.setText(String.format("%.1f miles", mTrips.get(position).getMiles()));

        return view;
    }

    private String formatDate(String dateString, String format){
        Date date;
        try {
            date = TripDataItem.DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US); // 3-letter month name & 2-char day of month
        return formatter.format(date);
    }
}
