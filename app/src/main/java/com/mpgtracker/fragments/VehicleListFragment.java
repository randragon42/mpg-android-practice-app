package com.mpgtracker.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mpgtracker.R;
import com.mpgtracker.adapters.VehicleListAdapter;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.vehicle.Vehicle;

import java.util.List;

public class VehicleListFragment extends BaseFragment {

    private VehicleViewModel mVehicleViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConstraintLayout mEmptyView;

    @Override
    protected String getTitle() { return "Vehicles"; }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_list, container, false);

        List<Vehicle> vehicles = mVehicleViewModel.getAllVehicles().getValue();

        mRecyclerView = view.findViewById(R.id.vehicle_recycler_view);
        mEmptyView = view.findViewById(R.id.empty_view);

        if(vehicles == null || vehicles.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }

        // Set up RecyclerView
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VehicleListAdapter(vehicles);
        mRecyclerView.setAdapter(mAdapter);

        // TODO: Set up recyclerView onClickListener or equivalent

        // Set up Add A Vehicle Button
        Button addVehicleButton = view.findViewById(R.id.add_vehicle_button);
        addVehicleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNewVehicle();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.vehicle_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_new_vehicle:
                addNewVehicle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNewVehicle() {
        VehicleEditFragment vehicleEditFragment = new VehicleEditFragment();

        // TODO: add slide-in-up and slide-down-out animations
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, vehicleEditFragment, "edit_vehicle")
                .addToBackStack(null)
                .commit();
    }

    private void selectVehicle(Vehicle vehicle) {
        mVehicleViewModel.selectVehicle(vehicle);

        VehicleFragment vehicleFragment = new VehicleFragment();
        // TODO: add slide-in-up and slide-down-out animations
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, vehicleFragment, "vehicle")
                .addToBackStack(null)
                .commit();
    }
}
