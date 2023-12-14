package com.example.payapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.payapp.database.Receipt;

import java.util.List;

@Dao
public interface ReceiptDao {

    @Insert
    void insertReceipt(Receipt receipt);

    @Query("SELECT * FROM Receipt WHERE userName = :userName ORDER BY formattedDateTime DESC")
    List<Receipt> getReceiptsByUserName(String userName);

    @Query("SELECT * FROM Receipt")
    List<Receipt> getAllReceipts();
}