package com.jrodiz.sucursalesbr.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jrodiz.sucursalesbr.obj.Sucursal;

import java.util.List;

@Dao
public interface SucursalDao {
    @Query("SELECT * FROM Sucursal")
    LiveData<List<Sucursal>> getAllLive();

    @Query("SELECT * FROM Sucursal")
    List<Sucursal> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Sucursal... s);

    @Delete
    void delete(Sucursal s);
}