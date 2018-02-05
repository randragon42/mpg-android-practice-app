package com.example.joshgr.mpgtracker.activities;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.joshgr.mpgtracker.helpers.MpgDbHelper;
import com.example.joshgr.mpgtracker.R;
import com.example.joshgr.mpgtracker.fragments.TripListFragment;

public class MainActivity extends AppCompatActivity {

    public MpgDbHelper TripDatabase;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setupToolbar();

        // Set up Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        setupDrawerContent(mNavigationView);

        // TODO: update db to Rooms DAOs
        TripDatabase = new MpgDbHelper(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TripListFragment tripListFragment = new TripListFragment();
        fragmentTransaction.add(R.id.fragmentContainer, tripListFragment, "tripList");
        fragmentTransaction.commit();
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        // TODO: fragment transaction for selected fragment based on item.getItemId()
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    private void setupToolbar(){
        mToolbar.setNavigationIcon(R.drawable.hamburger_menu);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
}
