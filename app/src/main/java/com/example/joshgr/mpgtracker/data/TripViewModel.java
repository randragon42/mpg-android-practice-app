package com.example.joshgr.mpgtracker.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;


public class TripViewModel extends AndroidViewModel {

    private TripRepository mRepository;
    private LiveData<List<Trip>> mAllTrips;
    private final MutableLiveData<Trip> mSelectedTrip = new MutableLiveData<Trip>();

    public TripViewModel(Application application) {
        super(application);
        mRepository = new TripRepository(application);
        mAllTrips = mRepository.getAllTrips();
    }

    public LiveData<List<Trip>> getAllTrips() { return mAllTrips; }

    public void insert(Trip trip) { mRepository.insert(trip); }

    public void update(Trip trip) { mRepository.update(trip); }

    public void delete(List<Trip> trips) { mRepository.delete(trips); }

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
}
