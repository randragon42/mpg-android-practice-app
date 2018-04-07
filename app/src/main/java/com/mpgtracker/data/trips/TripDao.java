package com.mpgtracker.data.trips;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TripDao {

    @Query("SELECT * FROM trips WHERE vehicleId = :vehicleId ORDER BY date ASC")
    LiveData<List<Trip>> getAllTrips(int vehicleId);

    @Insert
    void insertTrip(Trip trip);

    @Update
    void updateTrip(Trip trip);

    @Delete
    void deleteTrips(List<Trip> trips);
}
