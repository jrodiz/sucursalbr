package com.jrodiz.business.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.jrodiz.business.model.Sucursal;
import com.jrodiz.common.Constants;

@Database(entities = {Sucursal.class}, version = Constants.Db.DB_VERSION, exportSchema = false)
public abstract class AppLocalDb extends RoomDatabase {

    public abstract SucursalDao getSucursalDao();

    private static AppLocalDb LOCAL_DATABASE = null;

    public synchronized static AppLocalDb getInstance(@NonNull final Context appContext) {
        if (LOCAL_DATABASE == null) {
            LOCAL_DATABASE = Room.<AppLocalDb>databaseBuilder(
                    appContext.getApplicationContext(),
                    AppLocalDb.class,
                    Constants.Db.DB_NAME
            ).fallbackToDestructiveMigration().build();
        }
        return LOCAL_DATABASE;
    }
}
