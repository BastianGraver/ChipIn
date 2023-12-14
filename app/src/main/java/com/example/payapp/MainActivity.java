package com.example.payapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.payapp.database.User;

import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets the network policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d("Bastian", "onCreate: ");

        // Load intent from login.
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        // Load account into program.

        try {
            // Create a KeyPair object for the specified wallet address
            KeyPair keyPair = KeyPair.fromAccountId(user.public_seed);

            // Set up a connection to the Stellar testnet Horizon server
            Server server = new Server("https://horizon-testnet.stellar.org");

            // Retrieve account information for the specified wallet
            AccountResponse account = server.accounts().account(keyPair.getAccountId());

            // Log account details
            Log.d("Bastian", "Account ID: " + account.getAccountId());
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

            TextView coinType = findViewById(R.id.CoinType);  // Note the capital 'C'
            TextView amount = findViewById(R.id.Amount);      // Note the capital 'A'
            TextView wallet_id = findViewById(R.id.Wallet_Id);

            amount.setText(account.getBalances()[0].getBalance());
            coinType.setText(account.getBalances()[0].getAssetCode().get());
            wallet_id.setText("Wallet id: \n" + user.public_seed);

        } catch (Exception e) {
            Log.e("Bastian", "Error retrieving account information.", e);
        }
        // Greats user.
        Toast.makeText(this, "Welcome " + user.name, Toast.LENGTH_LONG).show();

    }
}
