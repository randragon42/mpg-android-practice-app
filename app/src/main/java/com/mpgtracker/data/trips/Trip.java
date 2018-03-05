package com.mpgtracker.data.trips;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.mpgtracker.data.Converters;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName="trips")
@TypeConverters(Converters.class)
public class Trip implements Comparable<Trip>{

    @PrimaryKey(autoGenerate=true)
    public int id;

    private int vehicleId;

    public Date date;

    public double gallons;

    public double miles;

    public double tripCost;

    public double odometer;

    public boolean filledTank;

    @Ignore
    public Trip(int id, Date date, double gallons, double miles, double tripCost, double odometer, boolean filledTank, int carId){
        this(date, gallons, miles, tripCost, odometer, filledTank, carId);
        this.id = id;
    }

    public Trip(Date date, double gallons, double miles, double tripCost, double odometer, boolean filledTank, int vehicleId){
        this.date = date;
        this.gallons = gallons;
        this.miles = miles;
        this.tripCost = tripCost;
        this.odometer = odometer;
        this.filledTank = filledTank;
        this.vehicleId = vehicleId;
    }

    //TODO: Add getters and setters
    public int getId() {
        return id;
    }

    public int getVehicleId() { return vehicleId; }

    public Date getDate(){
        return date;
    }

    public String getFormattedDate(){ return DATE_FORMAT.format(date); }

    public double getGallons(){
        return gallons;
    }

    public double getMiles(){
        return miles;
    }

    public double getTripCost(){
        return tripCost;
    }

    public double getOdometer(){
        return odometer;
    }

    public boolean getFilledTank(){
        return filledTank;
    }

    public double getCostPerGallon(){
        return tripCost/gallons;
    }

    public double getMilesPerGallon(){
        if (filledTank) {
            return miles/gallons;
        }
        else {
            return -1;
        }
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public int compareTo(@NonNull Trip o) {
        if (date.getTime() > o.getDate().getTime()) {
            return 1;
        }
        else if (date.getTime() <  o.date.getTime()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
