package com.example.joshgr.mpgtracker;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
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
            tripList.add(createNewTrip());
        }

        TripArrayAdapter tripArrayAdapter = new TripArrayAdapter(view.getContext(), R.layout.trip_item, tripList);
        tripListView.setAdapter(tripArrayAdapter);
    }

    private TripDataItem createNewTrip(){
        return new TripDataItem(new Date(), 14.123, 403.2, 30.45);
    }
}
