package com.mpgtracker.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mpgtracker.R;
import com.mpgtracker.fragments.VehicleFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private final String VEHICLE_TAG = "vehicle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Action Bar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Launch opening fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new VehicleFragment(), VEHICLE_TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            if(null != currentFragment && currentFragment.onOptionsItemSelected(item)){
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
