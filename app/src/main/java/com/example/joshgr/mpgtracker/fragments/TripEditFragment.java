package com.example.joshgr.mpgtracker.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.joshgr.mpgtracker.helpers.MpgDbHelper;
import com.example.joshgr.mpgtracker.R;
import com.example.joshgr.mpgtracker.data.TripDataItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TripEditFragment extends Fragment {

    TextView mDatePickerText;
    Calendar mCalendar;
    DatePickerDialog.OnDateSetListener mDate;
    SimpleDateFormat mDateFormat;

    public TripEditFragment() {
        // Required empty public constructor
        String format = "MM/dd/yyyy";
        mDateFormat = new SimpleDateFormat(format, Locale.US);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_edit, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Update Toolbar
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Create Trip");
        getActivity().setActionBar((toolbar));

        //Set up date picker
        mCalendar = Calendar.getInstance();
        mDatePickerText = (TextView) view.findViewById(R.id.datePicker);
        this.initDatePicker();

        Button saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = (ViewGroup) v.getParent();
                Date date = new Date();
                try {
                    date = mDateFormat.parse(((TextView)view.findViewById(R.id.datePicker)).getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                double miles = Double.parseDouble((((EditText)view.findViewById(R.id.milesEditText)).getText().toString()));
                double cost = Double.parseDouble((((EditText)view.findViewById(R.id.costEditText)).getText().toString()));
                double gallons = Double.parseDouble((((EditText)view.findViewById(R.id.gallonsEditText)).getText().toString()));

                final TripDataItem trip = new TripDataItem(0, date, gallons, miles, cost);

                // This was previously being run in a background thread which caused issues
                // when this fragment was popped and the TripListFragment resumed, fetching all
                // trip data points while this new one was being written to the db.
                MpgDbHelper db = new MpgDbHelper(getContext());
                db.addTrip(trip);

                getActivity().getFragmentManager().popBackStack();
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

    }

    private void initDatePicker(){
        if(mDatePickerText.getText() == ""){
            this.updateLabel();
        }
        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        mDatePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TripEditFragment.this.getContext(), mDate, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(){
        mDatePickerText.setText(mDateFormat.format(mCalendar.getTime()));
    }
}
