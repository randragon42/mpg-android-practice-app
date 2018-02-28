package com.mpgtracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mpgtracker.R;
import com.mpgtracker.data.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripArrayAdapter extends ArrayAdapter<Trip> {
    private List<Trip> mTrips;
    private Context mContext;

    public TripArrayAdapter(Context context, int listItemLayoutId, List<Trip> trips){
        super(context, listItemLayoutId);
        mContext = context;
        mTrips = trips;
    }

    @Nullable
    @Override
    public Trip getItem(int position) {
        return mTrips.get(position);
    }

    @Override
    public int getCount() {
        if (mTrips != null) {
            return mTrips.size();
        }
        else return 0;
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
            TextView tankFilled = (TextView) view.findViewById(R.id.tankFilled);

            TripViewHolder tripViewHolder = new TripViewHolder();
            tripViewHolder.Date = date;
            tripViewHolder.Year = year;
            tripViewHolder.MPG = mpg;
            tripViewHolder.Cost = cost;
            tripViewHolder.Gallons = gallons;
            tripViewHolder.CostPerGallon = costPerGallon;
            tripViewHolder.Miles = miles;
            tripViewHolder.Odometer = odometer;
            tripViewHolder.TankFilled = tankFilled;
            view.setTag(tripViewHolder);
        }

        TripViewHolder tripViewHolder = (TripViewHolder)view.getTag();

        // TODO: Add locale to formatting
        Trip trip = mTrips.get(position);
        tripViewHolder.Date.setText(formatDate(trip.getFormattedDate(), "MMM dd"));
        tripViewHolder.Year.setText(formatDate(trip.getFormattedDate(), "yyyy"));
        tripViewHolder.MPG.setText(String.format("%.2f mpg", trip.getMilesPerGallon()));
        tripViewHolder.Cost.setText(String.format("$%.2f", trip.getTripCost()));
        tripViewHolder.Gallons.setText(String.format("%.3f gal", trip.getGallons()));
        tripViewHolder.CostPerGallon.setText(String.format("$%.3f/gal", trip.getCostPerGallon()));
        tripViewHolder.Miles.setText(String.format("%.1f miles", trip.getMiles()));
        tripViewHolder.Odometer.setText(String.format("%.1f", trip.getOdometer()));
        tripViewHolder.TankFilled.setText(trip.getFilledTank() ? "Tank Filled" : "");
        return view;
    }

    public void setTrips(List<Trip> trips){
        mTrips = trips;
        notifyDataSetChanged();
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
