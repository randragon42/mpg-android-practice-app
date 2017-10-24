package com.example.joshgr.mpgtracker;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView tripListView = (ListView) view.findViewById(R.id.tripListView);
        MpgDbHelper db = new MpgDbHelper(view.getContext());
        ArrayList<TripDataItem> tripList = db.getAllTrips();

        if(tripList.size() == 0){
            tripList.add(createNewTrip(0));
            tripList.add(createNewTrip(1));
            tripList.add(createNewTrip(2));
            tripList.add(createNewTrip(3));
        }

        TripArrayAdapter tripArrayAdapter = new TripArrayAdapter(view.getContext(), R.layout.trip_item, tripList);
        tripListView.setAdapter(tripArrayAdapter);

        FloatingActionButton addTripButton = (FloatingActionButton) view.findViewById(R.id.addTripButton);
        addTripButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                AlertDialog ad = new AlertDialog.Builder(view.getContext())
//                        .create();
//                ad.setCancelable(false);
//                ad.setTitle("Dialog");
//                ad.setMessage("Message");
//                ad.setButton("ok", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                ad.show();
                  showTripEditFragment();
            }
        });
    }

    private void showTripEditFragment(){
        TripEditFragment tripEditFragment = new TripEditFragment();
        getFragmentManager().beginTransaction()
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
