package com.mpgtracker.adapters;


import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
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
import com.mpgtracker.helpers.UnitHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder> {
    private List<Trip> mAllTrips;
    private VehicleViewModel mVehicleViewModel;
    private FragmentActivity mActivity;
    private UnitHelper mUnitHelper;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mDate;
        public TextView mYear;
        public TextView mEfficiency;
        public TextView mCost;
        public TextView mVolume;
        public TextView mCostPerVolume;
        public TextView mDistance;
        public TextView mOdometer;
        public TextView mTankFilled;
        public IViewHolderClicks mListener;

        public ViewHolder(View view, IViewHolderClicks listener) {
            super(view);
            mListener = listener;
            mDate = view.findViewById(R.id.date);
            mYear = view.findViewById(R.id.year);
            mEfficiency = view.findViewById(R.id.mpg);
            mCost = view.findViewById(R.id.cost);
            mVolume = view.findViewById(R.id.volume);
            mCostPerVolume = view.findViewById(R.id.costPerVolume);
            mDistance = view.findViewById(R.id.distance);
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
        mUnitHelper = new UnitHelper(activity.getApplicationContext());
    }

    @Override
    public TripListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        ViewHolder vh = new ViewHolder(view, createViewClickHolder());
        return vh;
    }

    // TODO: fix string usage - use string resources and locale
    @Override
    public void onBindViewHolder(TripListAdapter.ViewHolder holder, int position) {
        final Trip trip = mAllTrips.get(position);

        holder.mDate.setText(formatDate(trip.getFormattedDate(), "MMM dd"));
        holder.mYear.setText(formatDate(trip.getFormattedDate(), "yyyy"));

        String volume = mUnitHelper.getVolumeLabel();
        String distance = mUnitHelper.getDistanceLabel();
        String efficiency = mUnitHelper.getEfficiencyLabel();

        holder.mCost.setText(String.format("$%.2f", trip.getTripCost()));
        holder.mVolume.setText(String.format("%.3f " + volume, trip.getVolume()));
        holder.mCostPerVolume.setText(String.format("$%.3f/" + volume, trip.getCostPerVolume()));
        holder.mDistance.setText(String.format("%.1f " + distance, trip.getDistance()));
        holder.mOdometer.setText(String.format("%.1f", trip.getOdometer()));

        if (trip.getFilledTank()) {
            holder.mEfficiency.setText(String.format("%.2f " + efficiency, trip.getEfficiency()));
            holder.mTankFilled.setText(mActivity.getResources().getString(R.string.tank_filled_notification));
        } else {
            holder.mEfficiency.setText("");
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
