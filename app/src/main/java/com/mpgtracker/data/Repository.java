package com.mpgtracker.data;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.mpgtracker.data.expenses.Expense;
import com.mpgtracker.data.expenses.ExpenseDao;
import com.mpgtracker.data.trips.Trip;
import com.mpgtracker.data.trips.TripDao;
import com.mpgtracker.data.vehicle.Vehicle;
import com.mpgtracker.data.vehicle.VehicleDao;

import java.util.List;

public class Repository {

    private int mVehicleId;

    private VehicleDao mVehicleDao;
    private TripDao mTripDao;
    private ExpenseDao mExpenseDao;

    private LiveData<List<Vehicle>> mAllVehicles;
    private LiveData<List<Trip>> mAllTrips;
    private LiveData<List<Expense>> mAllExpenses;


    public Repository(Application application, int vehicleId) {
        mVehicleId = vehicleId;
        Database db = Database.getTripsDatabase(application);
        // Vehicles
        mVehicleDao = db.vehicleDao();
        mAllVehicles = mVehicleDao.getAllVehicles();

        // Trips
        mTripDao = db.tripDao();
        mAllTrips = mTripDao.getAllTrips(mVehicleId);

        // Expenses
        mExpenseDao = db.expenseDao();
        mAllExpenses = mExpenseDao.getAllExpenses(mVehicleId);
    }

    // Vehicles
    public LiveData<List<Vehicle>> getAllVehicles() { return mAllVehicles; }
    public void insertVehicle(Vehicle vehicle) { new insertVehicleAsyncTask(mVehicleDao).execute(vehicle); }
    public void updateVehicle(Vehicle vehicle) { new updateVehicleAsyncTask(mVehicleDao).execute(vehicle); }
    public void deleteVehicles(List<Vehicle> vehicles) { new deleteVehicleAsyncTask(mVehicleDao).execute(vehicles); }

    // Trips
    public LiveData<List<Trip>> getAllTrips() { return mAllTrips; }
    public void insertTrip(Trip trip) {
        new insertTripAsyncTask(mTripDao).execute(trip);
    }
    public void updateTrip(Trip trip) { new updateTripAsyncTask(mTripDao).execute(trip); }
    public void deleteTrips(List<Trip> trips) { new deleteTripAsyncTask(mTripDao).execute(trips); }

    // Expenses
    public LiveData<List<Expense>> getAllExpenses() { return mAllExpenses; }
    public void insertExpense(Expense expense) { new insertExpenseAsyncTask(mExpenseDao).execute(expense); }
    public void updateExpense(Expense expense) { new updateExpenseAsyncTask(mExpenseDao).execute(expense); }
    public void deleteExpenses(List<Expense> expenses) { new deleteExpensesAsyncTask(mExpenseDao).execute(expenses); }

    // TODO: Use RxJava?
    // Async Tasks

    // Vehicles
    private static class insertVehicleAsyncTask extends AsyncTask<Vehicle, Void, Void> {
        private VehicleDao mVehicleDao;

        insertVehicleAsyncTask(VehicleDao dao) {
            mVehicleDao = dao;
        }
        @Override
        protected Void doInBackground(final Vehicle... params) {
            mVehicleDao.insertVehicle(params[0]);
            return null;
        }
    }
    private static class updateVehicleAsyncTask extends AsyncTask<Vehicle, Void, Void> {
        private VehicleDao mVehicleDao;
        updateVehicleAsyncTask(VehicleDao dao) {
            mVehicleDao = dao;
        }
        @Override
        protected Void doInBackground(final Vehicle... params) {
            mVehicleDao.updateVehicle(params[0]);
            return null;
        }
    }
    private static class deleteVehicleAsyncTask extends AsyncTask<List<Vehicle>, Void, Void> {
        private VehicleDao mVehicleDao;
        deleteVehicleAsyncTask(VehicleDao dao) {
            mVehicleDao = dao;
        }
        @Override
        protected Void doInBackground(final List<Vehicle>... params) {
            mVehicleDao.deleteVehicles(params[0]);
            return null;
        }
    }

    // Trips
    private static class insertTripAsyncTask extends AsyncTask<Trip, Void, Void> {
        private TripDao mTripDao;

        insertTripAsyncTask(TripDao dao) {
            mTripDao = dao;
        }
        @Override
        protected Void doInBackground(final Trip... params) {
            mTripDao.insertTrip(params[0]);
            return null;
        }
    }
    private static class updateTripAsyncTask extends AsyncTask<Trip, Void, Void> {
        private TripDao mAsyncTaskDao;
        updateTripAsyncTask(TripDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Trip... params) {
            mAsyncTaskDao.updateTrip(params[0]);
            return null;
        }
    }
    private static class deleteTripAsyncTask extends AsyncTask<List<Trip>, Void, Void> {
        private TripDao mAsyncTaskDao;
        deleteTripAsyncTask(TripDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final List<Trip>... params) {
            mAsyncTaskDao.deleteTrips(params[0]);
            return null;
        }
    }


    // Expenses
    private static class insertExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao mExpenseDao;

        insertExpenseAsyncTask(ExpenseDao dao) {
            mExpenseDao = dao;
        }
        @Override
        protected Void doInBackground(final Expense... params) {
            mExpenseDao.insertExpense(params[0]);
            return null;
        }
    }
    private static class updateExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao mExpenseDao;
        updateExpenseAsyncTask(ExpenseDao dao) {
            mExpenseDao = dao;
        }
        @Override
        protected Void doInBackground(final Expense... params) {
            mExpenseDao.updateExpense(params[0]);
            return null;
        }
    }
    private static class deleteExpensesAsyncTask extends AsyncTask<List<Expense>, Void, Void> {
        private ExpenseDao mExpenseDao;
        deleteExpensesAsyncTask(ExpenseDao dao) {
            mExpenseDao = dao;
        }
        @Override
        protected Void doInBackground(final List<Expense>... params) {
            mExpenseDao.deleteExpenses(params[0]);
            return null;
        }
    }
}
