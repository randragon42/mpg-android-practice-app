package com.example.joshgr.mpgtracker;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toolbar;

public class MainActivity extends Activity {

    public MpgDbHelper TripDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        TripDatabase = new MpgDbHelper(this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TripListFragment tripListFragment = new TripListFragment();
        fragmentTransaction.add(R.id.fragmentContainer, tripListFragment, "tripList");
        fragmentTransaction.commit();
    }
}
