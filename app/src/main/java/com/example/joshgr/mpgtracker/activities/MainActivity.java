package com.example.joshgr.mpgtracker.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.joshgr.mpgtracker.helpers.MpgDbHelper;
import com.example.joshgr.mpgtracker.R;
import com.example.joshgr.mpgtracker.fragments.TripListFragment;

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
