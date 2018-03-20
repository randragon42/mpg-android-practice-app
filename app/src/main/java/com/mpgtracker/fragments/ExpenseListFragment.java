package com.mpgtracker.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mpgtracker.R;
import com.mpgtracker.adapters.ExpenseListAdapter;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.expenses.Expense;

import java.util.List;

public class ExpenseListFragment extends BaseFragment {
    private VehicleViewModel mVehicleViewModel;
    private RecyclerView mRecyclerView;
    private ExpenseListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConstraintLayout mEmptyView;

    @Override
    protected String getTitle() { return null; }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // Get ViewModel
        mVehicleViewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Expense> expenses = mVehicleViewModel.getAllExpenses().getValue();

        mRecyclerView = view.findViewById(R.id.expense_list_recycler);
        mEmptyView = view.findViewById(R.id.empty_expense_list_view);

        // Set up RecyclerView
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ExpenseListAdapter(expenses, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        // Hook up observable to adapter
        mVehicleViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable final List<Expense> expenses) {
                // Update the cached copy of the words in the adapter.
                mAdapter.updateDataSet(expenses);
                showAndHideList(expenses);
            }
        });

        showAndHideList(expenses);

        // Set up floating action button
        FloatingActionButton addExpenseFAB = view.findViewById(R.id.add_expense_button);
        addExpenseFAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showExpenseEditFragment(null);
            }
        });

        // Set up Add A Vehicle Button
        Button addExpenseButton = view.findViewById(R.id.add_first_expense_button);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showExpenseEditFragment(null);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void showAndHideList(List<Expense> expenses) {
        if(expenses == null || expenses.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void showExpenseEditFragment(Expense expense){
        ExpenseEditFragment expenseEditFragment = new ExpenseEditFragment();
        mVehicleViewModel.selectExpense(expense);

        // TODO: add slide-in-up and slide-down-out animations
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, expenseEditFragment, "edit_expense")
                .addToBackStack(null)
                .commit();
    }
}
