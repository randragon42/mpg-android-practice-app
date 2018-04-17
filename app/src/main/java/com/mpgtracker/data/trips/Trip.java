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

    public double volume;

    public double distance;

    public double tripCost;

    public double odometer;

    public boolean filledTank;

    @Ignore
    public Trip(int id, Date date, double volume, double distance, double tripCost, double odometer, boolean filledTank, int carId){
        this(date, volume, distance, tripCost, odometer, filledTank, carId);
        this.id = id;
    }

    public Trip(Date date, double volume, double distance, double tripCost, double odometer, boolean filledTank, int vehicleId){
        this.date = date;
        this.volume = volume;
        this.distance = distance;
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

    public double getVolume(){
        return volume;
    }

    public double getDistance(){
        return distance;
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

    public double getCostPerVolume(){
        return tripCost/ volume;
    }

    public double getEfficiency(){
        if (filledTank) {
            return distance / volume;
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
