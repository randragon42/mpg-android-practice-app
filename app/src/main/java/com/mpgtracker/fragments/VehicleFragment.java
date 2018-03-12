package com.mpgtracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
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

import com.mpgtracker.R;
import com.mpgtracker.data.VehicleViewModel;


public class VehicleFragment extends BaseFragment {

    private VehicleViewModel mVehicleViewModel;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;

    private final String TRIP_TAG = "tripList";
    private final String GRAPHS_TAG = "graphs";
    private final String STATS_TAG = "stats";
    private final String SETTINGS_TAG = "settings";

    @Override
    protected String getTitle() { return getResources().getString(R.string.trips_title); }

    public VehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);

        // Set CarId from previous activity
        mVehicleViewModel.setVehicleId(getActivity().getApplication(), mVehicleViewModel.getSelectedVehicle().getValue().id);

        // Set up Navigation on action bar
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle, container, false);

        setHasOptionsMenu(true);

        // Set up Navigation Drawer
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        mDrawerLayout = view.findViewById(R.id.vehicle_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView = view.findViewById(R.id.vehicle_navigation);
        setupDrawer(mNavigationView);


        // TODO: add slide-in-up and slide-down-out animations
        TripListFragment tripListFragment = new TripListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.vehicleFragmentContainer, tripListFragment, "trip_list")
                .commit();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    }

    private void launchFragment(Fragment fragment, String tag){
        //TODO: add slide-up and slide-down transition animations
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.vehicleFragmentContainer, fragment, tag)
                .commit();
    }
}
