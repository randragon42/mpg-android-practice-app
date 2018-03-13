package com.mpgtracker.adapters;


import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpgtracker.R;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.trips.Trip;
import com.mpgtracker.fragments.TripEditFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder> {
    private List<Trip> mAllTrips;
    private VehicleViewModel mVehicleViewModel;
    private FragmentActivity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mDate;
        public TextView mYear;
        public TextView mMpg;
        public TextView mCost;
        public TextView mGallons;
        public TextView mCostPerGallon;
        public TextView mMiles;
        public TextView mOdometer;
        public TextView mTankFilled;
        public IViewHolderClicks mListener;

        public ViewHolder(View view, IViewHolderClicks listener) {
            super(view);
            mListener = listener;
            mDate = view.findViewById(R.id.date);
            mYear = view.findViewById(R.id.year);
            mMpg = view.findViewById(R.id.mpg);
            mCost = view.findViewById(R.id.cost);
            mGallons = view.findViewById(R.id.gallons);
            mCostPerGallon = view.findViewById(R.id.costPerGallon);
            mMiles = view.findViewById(R.id.miles);
            mOdometer = view.findViewById(R.id.odometer);
            mTankFilled = view.findViewById(R.id.tankFilled);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onRecyclerViewClicked(v, getAdapterPosition());
        }

        public interface IViewHolderClicks {
            void onRecyclerViewClicked(View caller, int position);
        }
    }

    public TripListAdapter(List<Trip> allTrips, FragmentActivity activity) {
        mAllTrips = allTrips;
        mActivity = activity;
        mVehicleViewModel = ViewModelProviders.of(activity).get(VehicleViewModel.class);
    }

    @Override
    public TripListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        ViewHolder vh = new ViewHolder(view, createViewClickHolder());
        return vh;
    }

    @Override
    public void onBindViewHolder(TripListAdapter.ViewHolder holder, int position) {
        final Trip trip = mAllTrips.get(position);

        holder.mDate.setText(formatDate(trip.getFormattedDate(), "MMM dd"));
        holder.mYear.setText(formatDate(trip.getFormattedDate(), "yyyy"));
        holder.mCost.setText(String.format("$%.2f", trip.getTripCost()));
        holder.mGallons.setText(String.format("%.3f gal", trip.getGallons()));
        holder.mCostPerGallon.setText(String.format("$%.3f/gal", trip.getCostPerGallon()));
        holder.mMiles.setText(String.format("%.1f miles", trip.getMiles()));
        holder.mOdometer.setText(String.format("%.1f", trip.getOdometer()));

        if (trip.getFilledTank()) {
            holder.mMpg.setText(String.format("%.2f mpg", trip.getMilesPerGallon()));
            holder.mTankFilled.setText(mActivity.getResources().getString(R.string.tank_filled_notification));
        } else {
            holder.mMpg.setText("");
            holder.mTankFilled.setText("");
        }
    }

    @Override
    public int getItemCount() {
        if (mAllTrips == null) {
            return 0;
        }
        return mAllTrips.size();
    }

    public void updateDataSet(List<Trip> trips) {
        mAllTrips = trips;
        notifyDataSetChanged();
    }

    private ViewHolder.IViewHolderClicks createViewClickHolder() {
        return new ViewHolder.IViewHolderClicks() {

            @Override
            public void onRecyclerViewClicked(View caller, int position) {
                mVehicleViewModel.selectTrip(mAllTrips.get(position));

                TripEditFragment tripEditFragment = new TripEditFragment();
                mActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.fragmentContainer, tripEditFragment, "trip")
                        .addToBackStack(null)
                        .commit();
            }
        };
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
