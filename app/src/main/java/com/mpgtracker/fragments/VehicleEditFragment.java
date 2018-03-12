package com.mpgtracker.fragments;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.mpgtracker.R;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.vehicle.Vehicle;

import java.util.ArrayList;

public class VehicleEditFragment extends BaseFragment {

    private boolean mCreateMode;
    private ActionBar mActionBar;
    private VehicleViewModel mVehicleViewModel;
    private Vehicle mVehicle;

    private EditText mName;
    private EditText mDescription;
    private EditText mMake;
    private EditText mModel;
    private EditText mYear;
    private EditText mLicensePlate;
    private EditText mVin;
    private EditText mInsurancePolicy;
    private Switch mIsActive;

    @Override
    protected String getTitle() {
        if(mCreateMode){ return getResources().getString(R.string.create_vehicle_title); }
        else { return getResources().getString(R.string.edit_vehicle_title); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_edit, container, false);

        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);

        // Set up Navigation on action bar
        setHasOptionsMenu(true);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mVehicleViewModel.getSelectedVehicle().getValue() == null) {
            mCreateMode = true;
        }
        super.onViewCreated(view, savedInstanceState);

        mName = view.findViewById(R.id.name);
        mDescription = view.findViewById(R.id.description);
        mMake = view.findViewById(R.id.make);
        mModel = view.findViewById(R.id.model);
        mYear = view.findViewById(R.id.year);
        mLicensePlate = view.findViewById(R.id.license_plate);
        mVin = view.findViewById(R.id.vin);
        mInsurancePolicy = view.findViewById(R.id.insurance);
        mIsActive = view.findViewById(R.id.active);

        if(mCreateMode) {
           CardView deleteCard = view.findViewById(R.id.delete_vehicle_card);
           deleteCard.setVisibility(View.GONE);
        }
        else {
            mVehicle = mVehicleViewModel.getSelectedVehicle().getValue();

            mName.setText(mVehicle.name);
            mDescription.setText(mVehicle.description);
            mMake.setText(mVehicle.make);
            mModel.setText(mVehicle.model);
            mYear.setText(mVehicle.year);
            mLicensePlate.setText(mVehicle.licensePlate);
            mVin.setText(mVehicle.vin);
            mInsurancePolicy.setText(mVehicle.insurancePolicy);
            mIsActive.setChecked(mVehicle.active);

            final Button deleteButton = view.findViewById(R.id.delete_vehicle);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteVehicle();
                }
            });
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.vehicle_edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exitFragment();
                return true;
            case R.id.save_vehicle:
                saveVehicle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveVehicle() {

        Vehicle vehicle = validateVehicle();
        if(vehicle == null){
            return;
        }

        if(mCreateMode){
            mVehicleViewModel.insertVehicle(vehicle);
        }
        else{
            vehicle.id = mVehicleViewModel.getSelectedVehicle().getValue().id;
            mVehicleViewModel.updateVehicle(vehicle);
        }

        exitFragment();
    }

    private void exitFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private Vehicle validateVehicle() {

        String name = validateEditText(mName, "Please provide a name for your vehicle.");
        String description = mDescription.getText().toString();
        String make = mMake.getText().toString();
        String model = mModel.getText().toString();
        String year = mYear.getText().toString();
        String licensePlate = mLicensePlate.getText().toString();
        String vin = mVin.getText().toString();
        String insurancePolicy = mInsurancePolicy.getText().toString();
        boolean isActive = mIsActive.isChecked();

        Vehicle vehicle = new Vehicle(name, year, make, model, description, licensePlate, vin, insurancePolicy, isActive);

        if(name == null) {
            return null;
        }

        return vehicle;
    }

    private String validateEditText(EditText editText, String errorMessage) {
        String value = editText.getText().toString();

        if(value.length() == 0){
            editText.setError(errorMessage);
            return null;
        }
        else {
            return value;
        }
    }

    private void deleteVehicle(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        dialogBuilder.setTitle(getResources().getString(R.string.delete_vehicle_dialog_title))
                .setMessage(getResources().getString(R.string.delete_vehicle_dialog_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
                        vehicles.add(mVehicle);
                        mVehicleViewModel.deleteVehicles(vehicles);
                        Toast.makeText(getContext(), getResources().getString(R.string.vehicle_deleted), Toast.LENGTH_LONG).show();
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
}
