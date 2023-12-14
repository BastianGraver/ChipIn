package com.example.payapp.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

@Entity
public class Receipt {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String itemName;
    public double itemPrice;
    public String userName;
    public String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

}
