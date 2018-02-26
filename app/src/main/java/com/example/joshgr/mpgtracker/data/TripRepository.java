package com.example.joshgr.mpgtracker.data;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TripRepository {

    private TripDAO mTripDao;
    private LiveData<List<Trip>> mAllTrips;

    TripRepository(Application application) {
        TripsDatabase db = TripsDatabase.getTripsDatabase(application);
        mTripDao = db.tripDAO();
        mAllTrips = mTripDao.getAllTrips();
    }

    LiveData<List<Trip>> getAllTrips() { return mAllTrips; }

    public void insert(Trip trip) {
        new insertAsyncTask(mTripDao).execute(trip);
    }
    public void update(Trip trip) { new updateAsyncTask(mTripDao).execute(trip); }
    public void delete(List<Trip> trips) { new deleteAsyncTask(mTripDao).execute(trips); }



    private static class insertAsyncTask extends AsyncTask<Trip, Void, Void> {
        private TripDAO mAsyncTaskDao;
        insertAsyncTask(TripDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Trip... params) {
            mAsyncTaskDao.insertTrip(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Trip, Void, Void> {
        private TripDAO mAsyncTaskDao;
        updateAsyncTask(TripDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Trip... params) {
            mAsyncTaskDao.updateTrip(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<List<Trip>, Void, Void> {
        private TripDAO mAsyncTaskDao;
        deleteAsyncTask(TripDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final List<Trip>... params) {
            mAsyncTaskDao.deleteTrips(params[0]);
            return null;
        }
    }
}
