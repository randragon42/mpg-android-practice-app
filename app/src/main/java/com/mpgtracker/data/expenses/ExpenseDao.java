package com.mpgtracker.data.expenses;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY date ASC")
    LiveData<List<Expense>> getAllExpenses();

    @Insert
    void insertExpense(Expense expense);

    @Update
    void updateExpense(Expense expense);

    @Delete
    void deleteExpenses(List<Expense> expenses);
}
