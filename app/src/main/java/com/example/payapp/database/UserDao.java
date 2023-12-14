package com.example.payapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE Name = :userName")
    User getUserByName(String userName);

    @Query("SELECT Password FROM User WHERE Name = :userName")
    String getPasswordForUser(String userName);

    @Delete
    void deleteUser(User user);

    // Add more queries and operations as needed
}
