package com.mpgtracker.adapters;


import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpgtracker.R;
import com.mpgtracker.data.VehicleViewModel;
import com.mpgtracker.data.expenses.Expense;
import com.mpgtracker.fragments.ExpenseEditFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ViewHolder> {

    private List<Expense> mAllExpenses;
    private VehicleViewModel mVehicleViewModel;
    private FragmentActivity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mDate;
        public TextView mExpenseType;
        public TextView mExpenseTitle;
        public TextView mOdometer;
        public TextView mCost;
        public TextView mNote;
        public ExpenseListAdapter.ViewHolder.IViewHolderClicks mListener;

        public ViewHolder(View view, ExpenseListAdapter.ViewHolder.IViewHolderClicks listener) {
            super(view);
            mListener = listener;
            mDate = view.findViewById(R.id.date);
            mExpenseType = view.findViewById(R.id.expense_type);
            mExpenseTitle = view.findViewById(R.id.expense_title);
            mOdometer = view.findViewById(R.id.odometer);
            mCost = view.findViewById(R.id.cost);
            mNote = view.findViewById(R.id.note);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onRecyclerViewClicked(v, getAdapterPosition());
        }

        public interface IViewHolderClicks {
            void onRecyclerViewClicked(View caller, int position);
        }
    }

    public ExpenseListAdapter(List<Expense> allExpenses, FragmentActivity activity) {
        mAllExpenses = allExpenses;
        mActivity = activity;
        mVehicleViewModel = ViewModelProviders.of(activity).get(VehicleViewModel.class);
    }

    @Override
    public ExpenseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        ExpenseListAdapter.ViewHolder vh = new ExpenseListAdapter.ViewHolder(view, createViewClickHolder());
        return vh;
    }

    @Override
    public void onBindViewHolder(ExpenseListAdapter.ViewHolder holder, int position) {
        final Expense expense = mAllExpenses.get(position);

        holder.mDate.setText(formatDate(expense.date, "yyyy MM dd"));
        holder.mExpenseType.setText(expense.type);
        holder.mExpenseTitle.setText(expense.title);
        holder.mOdometer.setText(String.format("%.1f", expense.odometer));
        holder.mCost.setText(String.format("$%.2f", expense.cost));
        holder.mNote.setText(expense.note);
    }

    @Override
    public int getItemCount() {
        if (mAllExpenses == null) {
            return 0;
        }
        return mAllExpenses.size();
    }

    public void updateDataSet(List<Expense> expenses) {
        mAllExpenses = expenses;
        notifyDataSetChanged();
    }

    private ExpenseListAdapter.ViewHolder.IViewHolderClicks createViewClickHolder() {
        return new ExpenseListAdapter.ViewHolder.IViewHolderClicks() {

            @Override
            public void onRecyclerViewClicked(View caller, int position) {
                mVehicleViewModel.selectExpense(mAllExpenses.get(position));

                ExpenseEditFragment expenseEditFragment = new ExpenseEditFragment();
                mActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.fragmentContainer, expenseEditFragment, "expense")
                        .addToBackStack(null)
                        .commit();
            }
        };
    }

    private String formatDate(Date date, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
