package com.example.payapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "Name")
    public String name;

    @ColumnInfo(name = "Password")
    public String password;

    @ColumnInfo(name = "Private_Seed")
    public String private_seed;

    @ColumnInfo(name = "Public_Seed")
    public String public_seed;

}

