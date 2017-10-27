package com.example.joshgr.mpgtracker;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripListFragment extends Fragment {

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

        ListView tripListView = (ListView) view.findViewById(R.id.tripListView);

        // Update Toolbar
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Trip List");
        getActivity().setActionBar((toolbar));

        MpgDbHelper db = new MpgDbHelper(view.getContext());
        ArrayList<TripDataItem> tripList = db.getAllTrips();

        if(tripList.size() == 0){
            tripList.add(createNewTrip(0));
            tripList.add(createNewTrip(1));
            tripList.add(createNewTrip(2));
            tripList.add(createNewTrip(3));
            tripList.add(createNewTrip(4));
            tripList.add(createNewTrip(5));
            tripList.add(createNewTrip(6));
            tripList.add(createNewTrip(7));
            tripList.add(createNewTrip(8));
            tripList.add(createNewTrip(9));
            tripList.add(createNewTrip(10));
            tripList.add(createNewTrip(11));
        }

        TripArrayAdapter tripArrayAdapter = new TripArrayAdapter(view.getContext(), R.layout.trip_item, tripList);
        tripListView.setAdapter(tripArrayAdapter);

        FloatingActionButton addTripButton = (FloatingActionButton) view.findViewById(R.id.addTripButton);
        addTripButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTripEditFragment();
                //showTripEditPopUp(view);
            }
        });
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

    private TripDataItem createNewTrip(int hours){
        Date date = new Date();
        date.setHours(date.getHours() - hours);
        return new TripDataItem(date, 14.123, 403.2, 30.45);
    }
}
