package com.mpgtracker.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpgtracker.R;
import com.mpgtracker.data.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    class TripViewHolder extends RecyclerView.ViewHolder {
        private final TextView date;
        private final TextView year;
        private final TextView mpg;
        private final TextView cost;
        private final TextView gallons;
        private final TextView costPerGallon;
        private final TextView miles;
        private final TextView odometer;
        private final TextView tankFilled;

        private TripViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            year = itemView.findViewById(R.id.year);
            mpg = itemView.findViewById(R.id.mpg);
            cost = itemView.findViewById(R.id.cost);
            gallons = itemView.findViewById(R.id.gallons);
            costPerGallon = itemView.findViewById(R.id.costPerGallon);
            miles = itemView.findViewById(R.id.miles);
            odometer = itemView.findViewById(R.id.odometer);
            tankFilled = itemView.findViewById(R.id.tankFilled);
        }
    }

    private final LayoutInflater mInflater;
    private List<Trip> mTrips; // Cached copy of words

    public TripListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.trip_item, parent, false);
        return new TripViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TripViewHolder tripViewHolder, int position) {
        if (mTrips != null) {
            Trip trip = mTrips.get(position);
            tripViewHolder.date.setText(formatDate(trip.getFormattedDate(), "MMM dd"));
            tripViewHolder.year.setText(formatDate(trip.getFormattedDate(), "yyyy"));
            tripViewHolder.mpg.setText(String.format("%.2f mpg", trip.getMilesPerGallon()));
            tripViewHolder.cost.setText(String.format("$%.2f", trip.getTripCost()));
            tripViewHolder.gallons.setText(String.format("%.3f gal", trip.getGallons()));
            tripViewHolder.costPerGallon.setText(String.format("$%.3f/gal", trip.getCostPerGallon()));
            tripViewHolder.miles.setText(String.format("%.1f miles", trip.getMiles()));
            tripViewHolder.odometer.setText(String.format("%.1f", trip.getOdometer()));
            tripViewHolder.tankFilled.setText(trip.getFilledTank() ? "Tank Filled" : "");


        } else {
            // Covers the case of data not being ready yet.

        }
    }

    public void setTrips(List<Trip> trips){
        mTrips = trips;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mTrips has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTrips != null)
            return mTrips.size();
        else return 0;
    }

    private String formatDate(String dateString, String format){
        Date date;
        try {
            date = Trip.DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US); // 3-letter month name & 2-char day of month
        return formatter.format(date);
    }
}
