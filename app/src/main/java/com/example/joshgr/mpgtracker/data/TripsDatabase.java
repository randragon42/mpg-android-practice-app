package com.example.joshgr.mpgtracker.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;


@Database(entities = {TripEntity.class}, version = 1)
public abstract class TripsDatabase extends RoomDatabase {

    private static TripsDatabase INSTANCE;

    public abstract TripDAO tripDAO();

    public static TripsDatabase getTripsDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), TripsDatabase.class, "trips-database")
                            //.addMigrations(migration)
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            // TODO: move db access to worker thread. Still considering how best to proceed with that.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    static final Migration migration = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
