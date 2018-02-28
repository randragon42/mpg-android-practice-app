package com.mpgtracker.activities;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mpgtracker.fragments.GraphsFragment;
import com.mpgtracker.fragments.SettingsFragment;
import com.mpgtracker.fragments.StatsFragment;
import com.mpgtracker.R;
import com.mpgtracker.fragments.TripListFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    private final String TRIP_TAG = "tripList";
    private final String GRAPHS_TAG = "graphs";
    private final String STATS_TAG = "stats";
    private final String SETTINGS_TAG = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Action Bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set up Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        setupDrawer(mNavigationView);

        // Launch opening fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new TripListFragment(), TRIP_TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawer(NavigationView navigationView){
        // Listeners for drawer menu selection
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        switch(item.getItemId()){
                            case R.id.trips_navigation_item:    launchFragment(new TripListFragment(), TRIP_TAG);
                                                                break;
                            case R.id.graphs_navigation_item:   launchFragment(new GraphsFragment(), GRAPHS_TAG);
                                                                break;
                            case R.id.stats_navigation_item:    launchFragment(new StatsFragment(), STATS_TAG);
                                                                break;
                            case R.id.settings_navigation_item: launchFragment(new SettingsFragment(), SETTINGS_TAG);
                                                                break;
                            default:                            break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );

        // Hamburger menu button functionality
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mToolbar.setNavigationIcon(R.drawable.hamburger_menu);
    }

    private void launchFragment(Fragment fragment, String tag){
        //TODO: add slide-up and slide-down transition animations
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .commit();
    }
}
