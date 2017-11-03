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

            TextView mpg = (TextView) view.findViewById(R.id.mpg);
            TextView cost = (TextView) view.findViewById(R.id.cost);
            TextView gallons = (TextView) view.findViewById(R.id.gallons);
            TextView cost_per_gallon = (TextView) view.findViewById(R.id.cost_per_gallon);
            TextView miles = (TextView) view.findViewById(R.id.miles);

            TripViewHolder tripViewHolder = new TripViewHolder();
            tripViewHolder.MPG = mpg;
            tripViewHolder.Cost = cost;
            tripViewHolder.Gallons = gallons;
            tripViewHolder.Cost_Per_Gallon = cost_per_gallon;
            tripViewHolder.Miles = miles;
            view.setTag(tripViewHolder);
        }

        TripViewHolder tripViewHolder = (TripViewHolder)view.getTag();

        tripViewHolder.MPG.setText(String.format("%.2f", mTrips.get(position).getMilesPerGallon()));
        tripViewHolder.Cost.setText(String.format("$%.2f", mTrips.get(position).getTripCost()));
        tripViewHolder.Gallons.setText(String.format("%.3f gallons", mTrips.get(position).getGallons()));
        tripViewHolder.Cost_Per_Gallon.setText(String.format("$%.3f per gallon", mTrips.get(position).getCostPerGallon()));
        tripViewHolder.Miles.setText(String.format("%.1f miles", mTrips.get(position).getMiles()));

        return view;
    }
}
