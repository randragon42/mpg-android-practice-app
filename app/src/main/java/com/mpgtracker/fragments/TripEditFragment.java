package com.mpgtracker.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mpgtracker.R;
import com.mpgtracker.data.trips.Trip;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.vehicle.Vehicle;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TripEditFragment extends BaseFragment {

    private Trip mTrip;
    private ActionBar mActionBar;
    private VehicleViewModel mVehicleViewModel;
    private Calendar mCalendar;
    private DatePickerDialog.OnDateSetListener mDate;
    private int mId = -1;
    private boolean mCreateMode = false;

    private TextView mDatePicker;
    private EditText mMiles;
    private EditText mGallons;
    private EditText mCost;
    private EditText mOdometer;
    private CheckBox mFilledTank;
    private Button mDeleteTrip;

    public TripEditFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_edit, container, false);

        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);

        // Set up Navigation on action bar
        setHasOptionsMenu(true);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.arrow_back);

        return view;
    }

    @Override
    protected String getTitle() {
        if(mCreateMode){ return getResources().getString(R.string.create_trip_title); }
        else { return getResources().getString(R.string.edit_trip_title); }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(mVehicleViewModel.getSelectedTrip().getValue() == null){
            mCreateMode = true;
        }
        super.onViewCreated(view, savedInstanceState);

        //Set up date picker
        mCalendar = Calendar.getInstance();
        mDatePicker = view.findViewById(R.id.date_picker);
        this.initDatePicker();

        mMiles = view.findViewById(R.id.miles_edit_text);
        mGallons = view.findViewById(R.id.gallons_edit_text);
        mCost = view.findViewById(R.id.cost_edit_text);
        mOdometer = view.findViewById(R.id.odometer_edit_text);
        mFilledTank = view.findViewById(R.id.filled_tank_check_box);
        mDeleteTrip = view.findViewById(R.id.delete_trip);

        //Set up fields if editing existing trip
        if(mCreateMode){
            CardView deleteCard = view.findViewById(R.id.delete_trip_card);
            deleteCard.setVisibility(View.GONE);

            //Set filledTankCheckBox to preference setting
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean filledTankPref = prefs.getBoolean("default_tank_filled", true);
            mFilledTank.setChecked(filledTankPref);
        }
        else {
            mTrip = mVehicleViewModel.getSelectedTrip().getValue();
            mId = mTrip.getId();

            mDatePicker.setText(mTrip.getFormattedDate());
            mMiles.setText(String.format(Double.toString(mTrip.getMiles()), "%.1f"));
            mGallons.setText(String.format(Double.toString(mTrip.getGallons()), "%.3f"));
            mCost.setText(String.format(Double.toString(mTrip.getTripCost()), "%.2f"));
            mOdometer.setText(String.format(Double.toString(mTrip.getOdometer()), "%.1f"));
            mFilledTank.setChecked(mTrip.getFilledTank());

            mDeleteTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTrip();
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.trip_edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            case R.id.save_vehicle:
                saveTrip();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mVehicleViewModel.clearSelectedTrip();
        mActionBar.setHomeAsUpIndicator(R.drawable.hamburger_menu);
    }

    private void deleteTrip(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        dialogBuilder.setTitle(getResources().getString(R.string.delete_trip_dialog_title))
                .setMessage(getResources().getString(R.string.delete_trip_dialog_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Trip> trips = new ArrayList<Trip>();
                        trips.add(mTrip);
                        mVehicleViewModel.deleteTrips(trips);
                        Toast.makeText(getContext(), getResources().getString(R.string.trip_deleted), Toast.LENGTH_LONG).show();
                        exitFragment();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
    }

    private void saveTrip(){

        // Validate form
        final Trip trip = validateTrip();
        if(trip == null){
            return;
        }

        if(mCreateMode){
            mVehicleViewModel.insertTrip(trip);
        }
        else{
            trip.id = mId;
            mVehicleViewModel.updateTrip(trip);
        }

        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void exitFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private Trip validateTrip(){

        String dateString = mDatePicker.getText().toString();
        Date date = parseDate(dateString);
        double miles = validateEditTextDouble(mMiles, getResources().getString(R.string.miles_missing), getResources().getString(R.string.miles_correct_format));
        double cost = validateEditTextDouble(mCost, getResources().getString(R.string.cost_missing), getResources().getString(R.string.cost_correct_format));
        double gallons = validateEditTextDouble(mGallons, getResources().getString(R.string.gallons_missing), getResources().getString(R.string.gallons_correct_format));
        double odometer = validateEditTextDouble(mOdometer, getResources().getString(R.string.odometer_missing), getResources().getString(R.string.odometer_correct_format));
        boolean filledTank = mFilledTank.isChecked();

        if(miles <= 0 || cost <= 0 || gallons <= 0 || odometer <= 0){
            return null;
        }
        else{
            return new Trip(date, gallons, miles, cost, odometer, filledTank, mVehicleViewModel.getVehicleId());
        }
    }

    private double validateEditTextDouble(EditText editText, String missingMessage, String correctFormatMessage){
        double value = 0;
        try {
            value = Double.parseDouble(editText.getText().toString());
            if(value <= 0){
                editText.setError(correctFormatMessage);
            }
        } catch (java.lang.NumberFormatException e) {
            editText.setError(missingMessage);
        }

        return value;
    }

    private void initDatePicker(){
        if(mDatePicker.getText() == ""){
            this.updateDateLabel();
        }
        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TripEditFragment.this.getContext(), mDate, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateDateLabel(){
        mDatePicker.setText(Trip.DATE_FORMAT.format(mCalendar.getTime()));
    }

    private Date parseDate(String date) {
        try {
            return Trip.DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
