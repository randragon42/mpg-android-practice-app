package com.example.joshgr.mpgtracker.data;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TripDAO {

    @Query("SELECT * FROM trips ORDER BY date ASC")
    LiveData<List<Trip>> getAllTrips();

    @Insert
    void insertTrip(Trip trip);

    @Update
    void updateTrip(Trip trip);

    @Delete
    void deleteTrips(List<Trip> trips);
}
