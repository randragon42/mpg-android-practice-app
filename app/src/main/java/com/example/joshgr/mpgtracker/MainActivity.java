package com.example.joshgr.mpgtracker;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TripListFragment tripListFragment = new TripListFragment();
        fragmentTransaction.add(R.id.listFragmentContainer, tripListFragment, "tripList");
        fragmentTransaction.commit();
    }
}
