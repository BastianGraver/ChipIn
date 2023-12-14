package com.example.payapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StoreItemDao {

    @Insert
    void insert(StoreItem storeItem);

    @Query("SELECT * FROM Imada")
    List<StoreItem> getAllItems();

    // You can add other queries or operations as needed
}

