package com.example.payapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import androidx.room.PrimaryKey;

import com.example.payapp.Fragments.StoreItemAdapter;

@Entity(tableName = "Imada")
public class StoreItem {

    @PrimaryKey(autoGenerate = true)
    public long id; // Primary key, auto-generated
    @ColumnInfo(name = "itemName")
    public String itemName;
    @ColumnInfo(name = "price")
    public double price;
    @ColumnInfo(name = "image")
    public int image;
}


