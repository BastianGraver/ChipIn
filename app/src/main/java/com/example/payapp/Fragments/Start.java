package com.example.payapp.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.payapp.CreateUserActivity;
import com.example.payapp.LoginActivity;
import com.example.payapp.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCreateUser = findViewById(R.id.btnCreateUser);
        // Fading in animation for the buttons
        ObjectAnimator.ofFloat(btnLogin, "alpha", 0f, 1f).setDuration(1500).start();
        ObjectAnimator.ofFloat(btnCreateUser, "alpha", 0f, 1f).setDuration(1500).start();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle login button click
                startActivity(new Intent(Start.this, LoginActivity.class));
            }
        });

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle create user button click
                startActivity(new Intent(Start.this, CreateUserActivity.class));
            }
        });
    }

    // You can add these methods to handle button clicks if you set onClick in XML layout
    public void onLoginButtonClick(View view) {
        startActivity(new Intent(Start.this, LoginActivity.class));
    }

    public void onCreateUserButtonClick(View view) {
        startActivity(new Intent(Start.this, CreateUserActivity.class));
    }
}
