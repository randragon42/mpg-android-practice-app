package com.mpgtracker.data.vehicle;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.List;

@Dao
public interface VehicleDao {
    @Query("SELECT * FROM vehicles")
    LiveData<List<Vehicle>> getAllVehicles();

    @Insert
    void insertVehicle(Vehicle vehicle);

    @Update
    void updateVehicle(Vehicle vehicle);

    @Delete
    void deleteVehicles(List<Vehicle> vehicles);
}
