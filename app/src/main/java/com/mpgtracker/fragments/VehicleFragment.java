package com.mpgtracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
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

    private ActionBar mActionBar;
    private BottomNavigationView mBottomNavigationView;

    private final String TRIP_TAG = "trip_list";
    private final String STATS_TAG = "stats";
    private final String EXPENSES_TAG = "expenses";

    @Override
    protected String getTitle() { return mVehicleViewModel.getSelectedVehicle().getValue().name; }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);

        super.onCreate(savedInstanceState);

        // Set CarId from previous activity
        mVehicleViewModel.setVehicleId(getActivity().getApplication(), mVehicleViewModel.getSelectedVehicle().getValue().id);

        // Set up Navigation on action bar
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.arrow_back);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle, container, false);

        setHasOptionsMenu(true);


        mBottomNavigationView = view.findViewById(R.id.vehicle_bottom_navigation);
        setupBottomNavigationView();

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
    public void onDestroy() {
        super.onDestroy();
        mActionBar.setHomeAsUpIndicator(R.drawable.hamburger_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.trips_navigation_item: launchFragment(new TripListFragment(), TRIP_TAG);
                                break;
                            case R.id.expenses_navigation_item: launchFragment(new ExpenseListFragment(), EXPENSES_TAG);
                                break;
                            case R.id.stats_navigation_item: launchFragment(new StatsFragment(), STATS_TAG);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
    }

    private void launchFragment(Fragment fragment, String tag){
        //TODO: add slide-up and slide-down transition animations
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.vehicleFragmentContainer, fragment, tag)
                .commit();
    }
}
