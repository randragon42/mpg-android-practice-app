package com.mpgtracker.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.mpgtracker.data.expenses.Expense;
import com.mpgtracker.data.expenses.ExpenseDao;
import com.mpgtracker.data.trips.Trip;
import com.mpgtracker.data.trips.TripDao;
import com.mpgtracker.data.vehicle.Vehicle;
import com.mpgtracker.data.vehicle.VehicleDao;


@android.arch.persistence.room.Database(entities = {Vehicle.class, Trip.class, Expense.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database INSTANCE;

    public abstract VehicleDao vehicleDao();
    public abstract TripDao tripDao();
    public abstract ExpenseDao expenseDao();

    public static Database getTripsDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(), Database.class, "mpg-tracker-database")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
