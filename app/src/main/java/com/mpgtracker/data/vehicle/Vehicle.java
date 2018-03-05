package com.mpgtracker.data.vehicle;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName="vehicles")
public class Vehicle {

    @PrimaryKey(autoGenerate=true)
    public int id;

    public String name;
    public int year;
    public String make;
    public String model;
    public String primaryDriver;
    public String description;
    public String licensePlate;
    public String vin;
    public String insurancePolicy;
    public boolean active;

    public Vehicle(String name, int year, String make, String model, String primaryDriver, String description, String licensePlate, String vin, String insurancePolicy, boolean active) {
        this.name = name;
        this.year = year;
        this.make = make;
        this.model = model;
        this.primaryDriver = primaryDriver;
        this.description = description;
        this.licensePlate = licensePlate;
        this.vin = vin;
        this.insurancePolicy = insurancePolicy;
        this.active = active;
    }
}
