package com.example.joshgr.mpgtracker.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.joshgr.mpgtracker.helpers.MpgDbHelper;
import com.example.joshgr.mpgtracker.R;
import com.example.joshgr.mpgtracker.adapters.TripArrayAdapter;
import com.example.joshgr.mpgtracker.data.TripDataItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripListFragment extends BaseFragment {

    private ArrayList<TripDataItem> mTripList;
    private TripArrayAdapter mAdapter;
    @Override
    protected String getTitle() { return "Trips"; }

    public TripListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
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

        MpgDbHelper db = new MpgDbHelper(view.getContext());
        mTripList = db.getAllTrips();

        // Set up list adapter
        ListView tripListView = (ListView) view.findViewById(R.id.tripListView);
        mAdapter = new TripArrayAdapter(view.getContext(), R.layout.trip_item, mTripList);
        tripListView.setAdapter(mAdapter);
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
        FloatingActionButton addTripButton = (FloatingActionButton) view.findViewById(R.id.addTripButton);
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

        refreshTripList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
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

    private void refreshTripList(){
        MpgDbHelper db = new MpgDbHelper(getContext());
        mTripList = db.getAllTrips();
        mAdapter.notifyDataSetChanged();
    }

    private void showTripEditFragment(TripDataItem trip){
        TripEditFragment tripEditFragment = new TripEditFragment();

        if(trip != null){
            Bundle bundle = new Bundle();
            bundle.putDouble("cost", trip.getTripCost());
            bundle.putDouble("miles", trip.getMiles());
            bundle.putDouble("gallons", trip.getGallons());
            bundle.putString("date", trip.getDate());
            bundle.putInt("id", trip.getId());
            bundle.putBoolean("filledTank", trip.getFilledTank());
            bundle.putDouble("odometer", trip.getOdometer());
            tripEditFragment.setArguments(bundle);
        }

        getFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, tripEditFragment, "edit")
                            .addToBackStack(null)
                            .commit();
    }

    private void deleteTrip(final int id, final int pos){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        dialogBuilder.setTitle("Delete?")
                .setMessage("Are you sure you want to delete this trips? This action cannot be undone.")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MpgDbHelper db = new MpgDbHelper(getContext());
                        db.deleteTrip(id);
                        TripDataItem deletedTrip = mTripList.remove(pos);
                        mAdapter.remove(mAdapter.getItem(pos));
                        mAdapter.notifyDataSetChanged();
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
                        MpgDbHelper db = new MpgDbHelper(getContext());
                        db.deleteAllTrips();
                        mTripList.clear();
                        mAdapter.clear();
                        mAdapter.notifyDataSetChanged();
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
