package com.mpgtracker.adapters;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mpgtracker.R;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.vehicle.Vehicle;
import com.mpgtracker.fragments.VehicleEditFragment;
import com.mpgtracker.fragments.VehicleListFragment;

import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {
    private List<Vehicle> mAllVehicles;
    private VehicleViewModel mVehicleViewModel;
    private FragmentActivity mActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mVehicleName;
        public TextView mVehicleYear;
        public TextView mVehicleMake;
        public TextView mVehicleModel;
        public ImageButton mEditButton;
        public ViewHolder(View view) {
            super(view);
            mVehicleName = view.findViewById(R.id.vehicle_name);
            mVehicleYear = view.findViewById(R.id.vehicle_year);
            mVehicleMake = view.findViewById(R.id.vehicle_make);
            mVehicleModel = view.findViewById(R.id.vehicle_model);
            mEditButton = view.findViewById(R.id.edit_vehicle_button);
        }
    }

    public VehicleListAdapter(List<Vehicle> allVehicles, FragmentActivity activity) {
        mAllVehicles = allVehicles;
        mActivity = activity;
        mVehicleViewModel = ViewModelProviders.of(activity).get(VehicleViewModel.class);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VehicleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Vehicle vehicle = mAllVehicles.get(position);

        holder.mVehicleName.setText(vehicle.name);
        holder.mVehicleYear.setText(vehicle.year);
        holder.mVehicleMake.setText(vehicle.make);
        holder.mVehicleModel.setText(vehicle.model);

        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVehicleViewModel.selectVehicle(vehicle);
                // Launch VehicleEditFragment
                VehicleEditFragment vehicleEditFragment = new VehicleEditFragment();
                mActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.fragmentContainer, vehicleEditFragment, "edit_vehicle")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mAllVehicles == null) {
            return 0;
        }
        return mAllVehicles.size();
    }

    public void updateDataSet(List<Vehicle> vehicles) {
        mAllVehicles = vehicles;
        notifyDataSetChanged();
    }
}
