package com.example.joshgr.mpgtracker.fragments;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.joshgr.mpgtracker.helpers.MpgDbHelper;
import com.example.joshgr.mpgtracker.R;
import com.example.joshgr.mpgtracker.adapters.TripArrayAdapter;
import com.example.joshgr.mpgtracker.data.TripDataItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripListFragment extends Fragment {

    private ArrayList<TripDataItem> mTripList;
    private TripArrayAdapter mAdapter;

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

        // Update Toolbar
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Trip List");
        getActivity().setActionBar((toolbar));

        MpgDbHelper db = new MpgDbHelper(view.getContext());
        mTripList = db.getAllTrips();

        // Set up list adapter
        ListView tripListView = (ListView) view.findViewById(R.id.tripListView);
        mAdapter = new TripArrayAdapter(view.getContext(), R.layout.trip_item, mTripList);
        tripListView.setAdapter(mAdapter);
        tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "Hi", Toast.LENGTH_LONG).show();
            }
        });

        // Set up floating action button
        FloatingActionButton addTripButton = (FloatingActionButton) view.findViewById(R.id.addTripButton);
        addTripButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTripEditFragment();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MpgDbHelper db = new MpgDbHelper(getContext());
        mTripList = db.getAllTrips();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showTripEditPopUp(View view){
        View popUpView = getActivity().getLayoutInflater().inflate(R.layout.fragment_trip_edit, null);

        PopupWindow popupWindow = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void showTripEditFragment(){
        TripEditFragment tripEditFragment = new TripEditFragment();
        getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.fragmentContainer, tripEditFragment, "edit")
                            .addToBackStack(null)
                            .commit();
    }
}
