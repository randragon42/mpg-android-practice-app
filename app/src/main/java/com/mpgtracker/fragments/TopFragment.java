package com.mpgtracker.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.mpgtracker.R;

public class TopFragment extends BaseFragment{

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;
    private Toolbar mToolbar;

    private final String VEHICLES_TAG = "vehicle_list";
    private final String SETTINGS_TAG = "settings";

    @Override
    protected String getTitle() { return null; }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up Navigation on action bar
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        setHasOptionsMenu(true);
        mToolbar = getActivity().findViewById(R.id.toolbar);

        // Set up Navigation Drawer
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        mDrawerLayout = view.findViewById(R.id.top_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView = view.findViewById(R.id.top_navigation);
        setupDrawer(mNavigationView);


        // TODO: add slide-in-up and slide-down-out animations
        VehicleListFragment vehicleListFragment = new VehicleListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.top_fragment_container, vehicleListFragment, "vehicle_list")
                .commit();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        Spinner spinner = mToolbar.findViewWithTag("spinner_nav");
        mToolbar.removeView(spinner);
        mActionBar.setDisplayShowTitleEnabled(true);
        super.onResume();
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
                            case R.id.vehicles_navigation_item:    launchFragment(new VehicleListFragment(), VEHICLES_TAG);
                                break;
                            case R.id.settings_navigation_item: launchFragment(new SettingsFragment(), SETTINGS_TAG);
                                break;
                            default:
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    private void launchFragment(Fragment fragment, String tag){
        //TODO: add slide-up and slide-down transition animations
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.top_fragment_container, fragment, tag)
                .commit();
    }
}
