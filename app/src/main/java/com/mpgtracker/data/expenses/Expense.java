package com.mpgtracker.data.expenses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.mpgtracker.data.Converters;

import java.util.Date;

@Entity(tableName="expenses")
@TypeConverters(Converters.class)
public class Expense {

    @PrimaryKey(autoGenerate=true)
    public int id;

    public int vehicleId;
    public Date date;
    public String type;
    public String title;
    public double cost;
    public String note;
    public double odometer;

    public Expense(int vehicleId, Date date, String type, String title, double cost, String note, double odometer) {
        this.vehicleId = vehicleId;
        this.date = date;
        this.type = type;
        this.title = title;
        this.cost = cost;
        this.note = note;
        this.odometer = odometer;
    }

}
