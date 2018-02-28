package com.mpgtracker.fragments;


import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mpgtracker.R;
import com.mpgtracker.adapters.TripArrayAdapter;
import com.mpgtracker.data.Trip;
import com.mpgtracker.data.TripViewModel;

import java.util.ArrayList;
import java.util.List;


public class TripListFragment extends BaseFragment {

    private List<Trip> mTripList;
    private TripArrayAdapter mAdapter;
    private TripViewModel mTripViewModel;
    @Override
    protected String getTitle() { return getResources().getString(R.string.trips_title); }

    public TripListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // Get ViewModel
        mTripViewModel = ViewModelProviders.of(getActivity()).get(TripViewModel.class);
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

        // Set up list adapter
        ListView tripListView = (ListView) view.findViewById(R.id.tripListView);
        mTripList = mTripViewModel.getAllTrips().getValue();
        mAdapter = new TripArrayAdapter(view.getContext(), R.layout.trip_item, mTripList);
        tripListView.setAdapter(mAdapter);

        // Hook up observable to adapter
        mTripViewModel.getAllTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable final List<Trip> trips) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setTrips(trips);
                mTripList = trips;
            }
        });

        //Set up click events
        tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTripEditFragment(mTripList.get(position));
            }
        });
        tripListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                int tripId = mTripList.get(pos).getId();
                deleteTrip(tripId, pos);
                return true;
            }
        });

        // Set up floating action button
        FloatingActionButton addTripButton = view.findViewById(R.id.addTripButton);
        addTripButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTripEditFragment(null);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //refreshTripList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.trip_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_trips_setting:
                deleteAllTrips();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void refreshTripList(){
//        TripsDatabase db = TripsDatabase.getTripsDatabase(getContext());
//        mTripList = db.tripDAO().getAllTrips();
//        mAdapter.notifyDataSetChanged();
//    }

    private void showTripEditFragment(Trip trip){
        TripEditFragment tripEditFragment = new TripEditFragment();
        mTripViewModel.selectTrip(trip);

        // TODO: add slide-in-up and slide-down-out animations
        getFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, tripEditFragment, "edit")
                            .addToBackStack(null)
                            .commit();
    }


    // TODO: Can these two delete methods be combined?
    //  Planned redo of deleting trips - long selection will go into multi selection mode allowing
    //  user to delete 1 or more trip at a time
    private void deleteTrip(final int id, final int pos){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        dialogBuilder.setTitle("Delete?")
                .setMessage("Are you sure you want to delete this trips? This action cannot be undone.")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Trip deletedTrip = mTripList.get(pos);
                        ArrayList<Trip> trips = new ArrayList<Trip>();
                        trips.add(deletedTrip);
                        mTripViewModel.delete(trips);
                        Toast.makeText(getContext(), "Trip deleted", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
    }

    private void deleteAllTrips(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        dialogBuilder.setTitle("Delete All Trips?")
                .setMessage("Are you sure you want to delete all trips? This action cannot be undone.")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTripList.clear();
                        mAdapter.clear();
                        mTripViewModel.delete(mTripList);
                        Toast.makeText(getContext(), "All trips deleted", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
    }
}
