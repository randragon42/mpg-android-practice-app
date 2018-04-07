package com.mpgtracker.adapters;


import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
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
import com.mpgtracker.fragments.VehicleFragment;

import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {
    private List<Vehicle> mAllVehicles;
    private VehicleViewModel mVehicleViewModel;
    private FragmentActivity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mVehicleName;
        public TextView mVehicleYear;
        public TextView mVehicleMake;
        public TextView mVehicleModel;
        public ImageButton mEditButton;
        public IViewHolderClicks mListener;

        public ViewHolder(View view, IViewHolderClicks listener) {
            super(view);
            mListener = listener;
            mVehicleName = view.findViewById(R.id.vehicle_name);
            mVehicleYear = view.findViewById(R.id.vehicle_year);
            mVehicleMake = view.findViewById(R.id.vehicle_make);
            mVehicleModel = view.findViewById(R.id.vehicle_model);
            mEditButton = view.findViewById(R.id.edit_vehicle_button);

            mEditButton.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.edit_vehicle_button){
                mListener.onEditButtonClicked((ImageButton)v, getAdapterPosition());
            } else {
                mListener.onRecyclerViewClicked(v, getAdapterPosition());
            }
        }

        public interface IViewHolderClicks {
            void onRecyclerViewClicked(View caller, int position);
            void onEditButtonClicked(ImageButton callerImage, int position);
        }
    }

    public VehicleListAdapter(List<Vehicle> allVehicles, FragmentActivity activity) {
        mAllVehicles = allVehicles;
        mActivity = activity;
        mVehicleViewModel = ViewModelProviders.of(activity).get(VehicleViewModel.class);
    }

    @Override
    public VehicleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle, parent, false);
        ViewHolder vh = new ViewHolder(view, createViewClickHolder());
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Vehicle vehicle = mAllVehicles.get(position);

        holder.mVehicleName.setText(vehicle.name);
        holder.mVehicleYear.setText(vehicle.year);
        holder.mVehicleMake.setText(vehicle.make);
        holder.mVehicleModel.setText(vehicle.model);
    }

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

    private ViewHolder.IViewHolderClicks createViewClickHolder() {
        return new ViewHolder.IViewHolderClicks() {

            @Override
            public void onRecyclerViewClicked(View caller, int position) {
                mVehicleViewModel.selectVehicle(mAllVehicles.get(position));

                VehicleFragment vehicleFragment = new VehicleFragment();
                mActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.fragmentContainer, vehicleFragment, "vehicle")
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onEditButtonClicked(ImageButton callerImage, int position) {
                mVehicleViewModel.selectVehicle(mAllVehicles.get(position));

                VehicleEditFragment vehicleEditFragment = new VehicleEditFragment();
                mActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.fragmentContainer, vehicleEditFragment, "edit_vehicle")
                        .addToBackStack(null)
                        .commit();
            }
        };
    }
}
