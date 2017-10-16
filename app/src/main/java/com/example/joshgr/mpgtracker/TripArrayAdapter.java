package com.example.joshgr.mpgtracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class TripArrayAdapter extends ArrayAdapter<TripDataItem> {
    private ArrayList<TripDataItem> mTrips;
    private Context mContext;

    public TripArrayAdapter(Context context, int listItemLayoutId, ArrayList<TripDataItem> trips){
        super(context, listItemLayoutId, trips);
        mContext = context;
        mTrips = trips;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rowView = inflater.inflate(R.layout.trip_item, parent, false);

        TextView mpg = (TextView) rowView.findViewById(R.id.mpg);
        mpg.setText(String.format("%.2f", mTrips.get(position).getMilesPerGallon()));

        TextView cost = (TextView) rowView.findViewById(R.id.cost);
        cost.setText(String.format("$%.2f", mTrips.get(position).getTripCost()));

        TextView gallons = (TextView) rowView.findViewById(R.id.gallons);
        gallons.setText(String.format("%.3f gallons", mTrips.get(position).getGallons()));

        TextView cost_per_gallon = (TextView) rowView.findViewById(R.id.cost_per_gallon);
        cost_per_gallon.setText(String.format("$%.3f per gallon", mTrips.get(position).getCostPerGallon()));

        TextView miles = (TextView) rowView.findViewById(R.id.miles);
        miles.setText(String.format("%.1f miles", mTrips.get(position).getMiles()));

        return rowView;
    }
}
