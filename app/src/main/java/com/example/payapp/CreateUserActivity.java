package com.example.payapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.payapp.database.AppDatabase;
import com.example.payapp.database.User;
import com.example.payapp.database.UserDao;

public class CreateUserActivity extends AppCompatActivity {

    // Database variables
    private AppDatabase database;
    private UserDao userDao;

    private EditText editTextName;
    private EditText editTextPassword;
    private Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        initDatabase();
        initViews();
        initListeners();
    }

    private void initDatabase() {
        // Get connecting with database.
        database = AppDatabase.getInstance(getApplicationContext());
        userDao = database.userDao();
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonCreate = findViewById(R.id.buttonCreate);
    }

    private void initListeners() {
        buttonCreate.setOnClickListener(view -> createUser());
    }

    private void createUser() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String name = editTextName.getText().toString();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter both name and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userDao.getUserByName(name) != null) {
            Toast.makeText(this, "User with this name already exists", Toast.LENGTH_SHORT).show();
            return;
        }


        Toast.makeText(this, "Wait a second while we create your new account.", Toast.LENGTH_SHORT).show();
        User newUser = new StellarMaster().createUser(name, password);

        // You might want to generate and set private_seed and public_seed here

        userDao.insert(newUser);

        Toast.makeText(this, "User created successfully.", Toast.LENGTH_SHORT).show();

        // Optionally, you can finish the activity or navigate to another screen
        // finish();

        startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
    }
}
