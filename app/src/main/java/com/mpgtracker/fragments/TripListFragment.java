package com.mpgtracker.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mpgtracker.R;
import com.mpgtracker.adapters.TripListAdapter;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.trips.Trip;

import java.util.List;


public class TripListFragment extends BaseFragment {

    private VehicleViewModel mVehicleViewModel;
    private RecyclerView mRecyclerView;
    private TripListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConstraintLayout mEmptyView;

    @Override
    protected String getTitle() { return null; }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Trip> trips = mVehicleViewModel.getAllTrips().getValue();

        mRecyclerView = view.findViewById(R.id.trip_list_recycler);
        mEmptyView = view.findViewById(R.id.empty_trip_list_view);

        // Set up RecyclerView
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TripListAdapter(trips, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        // Hook up observable to adapter
        mVehicleViewModel.getAllTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable final List<Trip> trips) {
                // Update the cached copy of the words in the adapter.
                mAdapter.updateDataSet(trips);
                showAndHideList(trips);
            }
        });

        showAndHideList(trips);

        // Set up floating action button
        FloatingActionButton addTripFAB = view.findViewById(R.id.addTripButton);
        addTripFAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTripEditFragment(null);
            }
        });

        // Set up Add A Vehicle Button
        Button addTripButton = view.findViewById(R.id.add_trip_button);
        addTripButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTripEditFragment(null);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        inflater.inflate(R.menu.trip_list_menu, menu);
    }

    private void showAndHideList(List<Trip> trips) {
        if(trips == null || trips.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void showTripEditFragment(Trip trip){
        TripEditFragment tripEditFragment = new TripEditFragment();
        mVehicleViewModel.selectTrip(trip);

        // TODO: add slide-in-up and slide-down-out animations
        getFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, tripEditFragment, "edit")
                            .addToBackStack(null)
                            .commit();
    }


}
