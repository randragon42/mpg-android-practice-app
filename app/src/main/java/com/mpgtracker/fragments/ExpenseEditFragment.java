package com.mpgtracker.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mpgtracker.R;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.expenses.Expense;
import com.mpgtracker.data.trips.Trip;

import java.security.spec.ECParameterSpec;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpenseEditFragment extends BaseFragment {

    private Expense mExpense;
    private boolean mCreateMode;
    private VehicleViewModel mVehicleViewModel;
    private ActionBar mActionBar;
    private Calendar mCalendar;
    private DatePickerDialog.OnDateSetListener mDate;
    private int mId;

    private Spinner mCategory;
    private EditText mTitle;
    private EditText mCost;
    private EditText mDatePicker;
    private EditText mOdometer;
    private EditText mNote;
    private Button mDeleteExpense;

    @Override
    protected String getTitle() {
        if(mCreateMode){ return getResources().getString(R.string.create_expense_title); }
        else { return getResources().getString(R.string.edit_expense_title); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense_edit, container, false);

        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);

        // Determine Create or Edit Mode
        if (mVehicleViewModel.getSelectedTrip().getValue() == null) {
            mCreateMode = true;
        }

        // Set up Navigation on action bar
        setHasOptionsMenu(true);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.arrow_back);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set up date picker
        mCalendar = Calendar.getInstance();
        mDatePicker = view.findViewById(R.id.date);
        this.initDatePicker();

        mTitle = view.findViewById(R.id.title);
        mCost = view.findViewById(R.id.cost);
        mOdometer = view.findViewById(R.id.odometer);
        mNote = view.findViewById(R.id.note);
        mDeleteExpense = view.findViewById(R.id.delete_expense);

        if(mCreateMode) {
            CardView deleteCard = view.findViewById(R.id.delete_expense_card);
            deleteCard.setVisibility(View.GONE);
        }
        else {
            mExpense = mVehicleViewModel.getSelectedExpense().getValue();
            mId = mExpense.id;

            mTitle.setText(mExpense.title);
            mCost.setText(String.format(Double.toString(mExpense.cost), "$.2f"));
            mOdometer.setText(String.format(Double.toString(mExpense.odometer), "%.1f"));
            mDatePicker.setText(mExpense.getFormattedDate());
            mNote.setText(mExpense.note);

            mDeleteExpense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteExpense();
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.expense_edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exitFragment();
                return true;
            case R.id.save_expense:
                saveExpense();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mVehicleViewModel.getSelectedVehicle().getValue().name);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }


    private void initDatePicker(){
        if(mDatePicker.getText().toString().equals("")){
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
                new DatePickerDialog(ExpenseEditFragment.this.getContext(), mDate, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

    private void exitFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void saveExpense(){

        // Validate form
        final Expense expense = validateExpense();
        if(expense == null){
            return;
        }

        if(mCreateMode){
            mVehicleViewModel.insertExpense(expense);
        }
        else{
            expense.id = mId;
            mVehicleViewModel.updateExpense(expense);
        }

        exitFragment();
    }

    // TODO: add validation checks as needed
    private Expense validateExpense() {
        //title cost odometer date note type
        String title = mTitle.getText().toString();
        String note = mNote.getText().toString();
        Date date = parseDate(mDatePicker.getText().toString());
        double cost = Double.parseDouble(mCost.getText().toString());
        double odometer = Double.parseDouble(mOdometer.getText().toString());

        return new Expense(mVehicleViewModel.getVehicleId(), date, "placeholder", title, cost, note, odometer);
    }

    private void deleteExpense(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        dialogBuilder.setTitle(getResources().getString(R.string.delete_expense_dialog_title))
                .setMessage(getResources().getString(R.string.delete_expense_dialog_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Expense> expenses = new ArrayList<>();
                        expenses.add(mExpense);
                        mVehicleViewModel.deleteExpenses(expenses);
                        Toast.makeText(getContext(), getResources().getString(R.string.expense_deleted), Toast.LENGTH_LONG).show();
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
