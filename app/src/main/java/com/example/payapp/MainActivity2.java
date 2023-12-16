package com.example.payapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.payapp.database.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payapp.ui.main.SectionsPagerAdapter;
import com.example.payapp.databinding.ActivityMain2Binding;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Server;
import org.stellar.sdk.responses.AccountResponse;

public class MainActivity2 extends AppCompatActivity{

    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");


        // Greats user.
        Toast.makeText(this, "Welcome " + user.name, Toast.LENGTH_LONG).show();
        Log.d("Bastia", "onCreate: From intent " + user);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;
        Log.d("Bastian", "onCreate: Hello from main");



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Set text for Saldo
        // Sets the network policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            // Create a KeyPair object for the specified wallet address
            KeyPair keyPair = KeyPair.fromAccountId(user.public_seed);

            // Set up a connection to the Stellar testnet Horizon server
            Server server = new Server("https://horizon-testnet.stellar.org");

            // Retrieve account information for the specified wallet
            AccountResponse account = server.accounts().account(keyPair.getAccountId());

            // Log account details
            Log.d("Bastian", "Account ID: " + account.getAccountId() + " " + user.private_seed);
            Log.d("Bastian", "Balances:");

            for (AccountResponse.Balance balance : account.getBalances()) {
                Log.d("Bastian", String.format(
                        "Type: %s, Code: %s, Balance: %s",
                        balance.getAssetType(),
                        balance.getAssetCode(),
                        balance.getBalance()
                ));
            }

            // Change text on screen.

            TextView saldoText = findViewById(R.id.saldo);
            saldoText.setText("Saldo: " + account.getBalances()[0].getBalance() + " Tokens");


        } catch (Exception e) {
            Log.e("Bastian", "Error retrieving account information.", e);
        }
    }

    public void updateSaldoText(String newSaldo) {
        TextView saldoText = findViewById(R.id.saldo);
        saldoText.setText(newSaldo);
    }
}
