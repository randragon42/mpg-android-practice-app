package com.example.joshgr.mpgtracker.data;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TripDAO {

    @Query("SELECT * FROM trips")
    List<TripEntity> getAll();

    @Insert
    void insertTrip(TripEntity trip);

    @Update
    void updateTrip(TripEntity trip);

    @Delete
    void deleteTrip(TripEntity trip);
}
