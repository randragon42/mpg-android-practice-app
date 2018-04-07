package com.mpgtracker.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.mpgtracker.data.expenses.Expense;
import com.mpgtracker.data.trips.Trip;
import com.mpgtracker.data.trips.TripStats;
import com.mpgtracker.data.vehicle.Vehicle;

import java.util.List;


public class VehicleViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<Vehicle>> mAllVehicles;
    private LiveData<List<Trip>> mAllTrips;
    private LiveData<List<Expense>> mAllExpenses;
    private final MutableLiveData<Vehicle> mSelectedVehicle = new MutableLiveData<Vehicle>();
    private final MutableLiveData<Trip> mSelectedTrip = new MutableLiveData<Trip>();
    private final MutableLiveData<Expense> mSelectedExpense = new MutableLiveData<Expense>();
    private int mVehicleId;

    public VehicleViewModel(Application application) {
        super(application);

        mRepository = new Repository(application, mVehicleId);
        mAllTrips = mRepository.getAllTrips();
        mAllVehicles = mRepository.getAllVehicles();
        mAllExpenses = mRepository.getAllExpenses();
    }

    // Vehicles
    public LiveData<List<Vehicle>> getAllVehicles() { return mAllVehicles; }
    public void insertVehicle(Vehicle vehicle) { mRepository.insertVehicle(vehicle);}
    public void updateVehicle(Vehicle vehicle) { mRepository.updateVehicle(vehicle);}
    public void deleteVehicles(List<Vehicle> vehicles) { mRepository.deleteVehicles(vehicles);}
    public int getVehicleId() { return mVehicleId; }
    public void setVehicleId(Application application, int vehicleId) {
        mVehicleId = vehicleId;
        mRepository = new Repository(application, vehicleId);
        mAllTrips = mRepository.getAllTrips();
    }

    // Pass selected vehicle between VehicleListFragment and VehicleFragment
    public void selectVehicle(Vehicle vehicle) { mSelectedVehicle.setValue(vehicle); }
    public LiveData<Vehicle> getSelectedVehicle() { return mSelectedVehicle; }
    public void clearSelectedVehicle() { mSelectedVehicle.setValue(null); }

    // Trips
    public LiveData<List<Trip>> getAllTrips() { return mAllTrips; }
    public void insertTrip(Trip trip) { mRepository.insertTrip(trip); }
    public void updateTrip(Trip trip) { mRepository.updateTrip(trip); }
    public void deleteTrips(List<Trip> trips) { mRepository.deleteTrips(trips); }

    // Pass selected trip between TripListFragment and EditTripFragment
    public void selectTrip(Trip trip) {
        mSelectedTrip.setValue(trip);
    }
    public LiveData<Trip> getSelectedTrip() {
        return mSelectedTrip;
    }
    public void clearSelectedTrip() {
        mSelectedTrip.setValue(null);
    }

    public TripStats getTripStats() {
        if (mAllTrips.getValue() != null) {
            return new TripStats(mAllTrips.getValue());
        } else {
            return null;
        }
    }

    // Expenses
    public LiveData<List<Expense>> getAllExpenses() { return mAllExpenses; }
    public void insertExpense(Expense expense) { mRepository.insertExpense(expense); }
    public void updateExpense(Expense expense) { mRepository.updateExpense(expense); }
    public void deleteExpenses(List<Expense> expenses) { mRepository.deleteExpenses(expenses); }

    // Pass selected expense between ExpenseListFragment and EditExpenseFragment
    public void selectExpense(Expense expense) { mSelectedExpense.setValue(expense); }
    public LiveData<Expense> getSelectedExpense() { return mSelectedExpense; }
    public void clearSelectedExpense() { mSelectedExpense.setValue(null); }
}
