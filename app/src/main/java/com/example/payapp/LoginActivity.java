package com.example.payapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.payapp.database.AppDatabase;
import com.example.payapp.database.User;
import com.example.payapp.database.UserDao;

public class LoginActivity extends AppCompatActivity {

    // Database variables.
    private AppDatabase database;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get connecting with database.
        database = AppDatabase.getInstance(getApplicationContext());
        userDao = database.userDao();

        final EditText editPassword = findViewById(R.id.Password);
        final EditText editUsername = findViewById(R.id.Username);
        Button btnNavigate = findViewById(R.id.btnNavigate);


        // Programming for button.
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                Log.d("Bastian", "onClick: " + name + " " + password);
                // Check if user is in database
                Log.d("Bastian", "onClick: " + userDao.getPasswordForUser(name) + " " + password);
                try {


                    // Check if password matches user name
                    if (userDao.getPasswordForUser(name).equals(password)) {
                        User user = userDao.getUserByName(name);

                        if (user != null) {
                            // Pass user to the main activity

                            Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        }
                    }
                }
                // Login not found
                catch (Exception e){
                    Log.d("Bastian", "onClick: Error " + e.getMessage());
                    Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}