package com.mpgtracker.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
    private VehicleListAdapter mAdapter;
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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Vehicle> vehicles = mVehicleViewModel.getAllVehicles().getValue();

        mRecyclerView = view.findViewById(R.id.vehicle_recycler_view);
        mEmptyView = view.findViewById(R.id.empty_vehicle_list_view);

        // Set up RecyclerView
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VehicleListAdapter(vehicles, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        // Hook up observable to adapter
        mVehicleViewModel.getAllVehicles().observe(this, new Observer<List<Vehicle>>() {
            @Override
            public void onChanged(@Nullable final List<Vehicle> vehicles) {
                // Update the cached copy of the words in the adapter.
                mAdapter.updateDataSet(vehicles);
                showAndHideList(vehicles);
            }
        });

        showAndHideList(vehicles);

        // Set up Add A Vehicle Button
        Button addVehicleButton = view.findViewById(R.id.add_vehicle_button);
        addVehicleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNewVehicle();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mVehicleViewModel.clearSelectedVehicle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.vehicle_list_menu, menu);
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

    private void showAndHideList(List<Vehicle> vehicles) {
        if(vehicles == null || vehicles.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void addNewVehicle() {
        VehicleEditFragment vehicleEditFragment = new VehicleEditFragment();

        // TODO: add slide-in-up and slide-down-out animations
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.fragmentContainer, vehicleEditFragment, "edit_vehicle")
                .addToBackStack(null)
                .commit();
    }
}
