package com.example.payapp;

import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import com.example.payapp.database.User;

import org.stellar.sdk.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;


public class StellarMaster {
    // Global variabels
    private String serverUrl = "https://horizon-testnet.stellar.org";
    private String TAG = "Bastian";

    public String DistPrivateWallet = "SA6CSJAR2JNK7XP2MCPTG3QM7URQMLAHKSNCDHBVGHA7TYVEW7AH2S5M";


    public KeyPair createKeyPair(){
        KeyPair newKeyPair = KeyPair.random();
        Log.d("Bastian", "New Public key: " +newKeyPair.getAccountId() + " New Private key" + new String(newKeyPair.getSecretSeed()));
        return newKeyPair;
    }

    public User createUser(String name, String password){
        // Sets the network policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        KeyPair keyPair = createKeyPair();

        // Set up new account on testnet.
        try {
            String friendbotUrl = String.format(
                    "https://friendbot.stellar.org/?addr=%s",
                    keyPair.getAccountId());
            InputStream response = new URL(friendbotUrl).openStream();
            String body = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
            //System.out.println("SUCCESS! You have a new account :)\n" + body);
        }
        catch (Exception e){
            System.out.println("Error");
        }

        // Create trust.
        createTrust(keyPair.getSecretSeed());

        // Log wallet
        try {
            // Create a KeyPair object for the specified wallet address

            // Set up a connection to the Stellar testnet Horizon server
            Server server = new Server(serverUrl);

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
        } catch (Exception e) {
            Log.e("Bastian", "Error retrieving account information.", e);
        }
        // All good return user
        Log.d(TAG, "createUser: Public " + keyPair.getAccountId() + " Private " + new String(keyPair.getSecretSeed()));
        User newUser = new User();
        newUser.name = name;
        Log.d(TAG, "createUser: User");
        newUser.public_seed = keyPair.getAccountId();
        Log.d(TAG, "createUser: Public");
        newUser.private_seed = new String(keyPair.getSecretSeed());
        Log.d(TAG, "createUser: Secret");
        newUser.password = password;
        Log.d(TAG, "createUser: Password");

        return newUser;
    }

    public void sendTokens(float amount, String sourcePrivatWallet, String destinationPublicWallet, String message){
        try {
            Server server = new Server(serverUrl);

            KeyPair source = KeyPair.fromSecretSeed(sourcePrivatWallet);
            KeyPair destination = KeyPair.fromAccountId(destinationPublicWallet);

            // First, check to make sure that the destination account exists.
            // You could skip this, but if the account does not exist, you will be charged
            // the transaction fee when the transaction fails.
            // It will throw HttpResponseException if account does not exist or there was another error.
            server.accounts().account(destination.getAccountId());
            Log.d(TAG, "sendTokens: Dest Account data loaded");

            Asset imadaCoin = new AssetTypeCreditAlphaNum12("ImadaCoin", "GDJOSFEOXHL7H4GLETNBECNGIFYY2WXEIYCG3SM6UK6WRDVFPWJVBYF7");
            // If there was no error, load up-to-date information on your account.
            AccountResponse sourceAccount = server.accounts().account(source.getAccountId());
            Log.d(TAG, "sendTokens: Source Account data loaded");

            // Start building the transaction.
            Transaction transaction = new Transaction.Builder(sourceAccount, Network.TESTNET)
                    .addOperation(new PaymentOperation.Builder(destination.getAccountId(), imadaCoin, Float.toString(amount)).build())
                    // A memo allows you to add your own metadata to a transaction. It's
                    // optional and does not affect how Stellar treats the transaction.
                    .addMemo(Memo.text("Payment: " + message))
                    // Wait a maximum of three minutes for the transaction
                    .setTimeout(180)
                    // Set the amount of lumens you're willing to pay per operation to submit your transaction
                    .setBaseFee(Transaction.MIN_BASE_FEE)
                    .build();

            // Sign the transaction to prove you are actually the person sending it.
            transaction.sign(source);

            // And finally, send it off to Stellar!
            try {
                SubmitTransactionResponse response = server.submitTransaction(transaction);
                Log.d(TAG, "sendTokens: " + response);
            } catch (Exception e) {
                Log.d(TAG, "sendTokens: " + e.getMessage());
                // If the result is unknown (no response body, timeout etc.) we simply resubmit
                // already built transaction:
                // SubmitTransactionResponse response = server.submitTransaction(transaction);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void createTrust(char[] secretWalletId){
        try {
            Server server = new Server(serverUrl);

            KeyPair source = KeyPair.fromSecretSeed("SDUAMYBOTBXDE7DMEDDWEGKFDGTRX2F3RSELE5ZZJ4TNSLVKPHGYFIJH");
            KeyPair destination = KeyPair.fromSecretSeed(secretWalletId);
            Log.d(TAG, "createTrust: Server and keypair good");

            // First, check to make sure that the destination account exists.
            // You could skip this, but if the account does not exist, you will be charged
            // the transaction fee when the transaction fails.
            // It will throw HttpResponseException if account does not exist or there was another error.
            server.accounts().account(destination.getAccountId());
            Log.d(TAG, "createTrust: Account info loaded");

            Asset imadaCoin = new AssetTypeCreditAlphaNum12("ImadaCoin", "GDJOSFEOXHL7H4GLETNBECNGIFYY2WXEIYCG3SM6UK6WRDVFPWJVBYF7");
            ChangeTrustOperation changeTrustOperation = new ChangeTrustOperation.Builder(ChangeTrustAsset.create(imadaCoin), "1000").build();
            // First, the receiving account must trust the asset
            Log.d(TAG, "createTrust: Creating trust");
            AccountResponse receiving = server.accounts().account(destination.getAccountId());
            Transaction allowAstroDollars = new Transaction.Builder(receiving, Network.TESTNET)
                    .addOperation(changeTrustOperation)
                    .setTimeout(180)
                    .addMemo(Memo.text("Setting trust from distro"))
                    .setBaseFee(Transaction.MIN_BASE_FEE)
                    .build();
            allowAstroDollars.sign(destination);
            Log.d(TAG, "createTrust: Trust sent");
            try {                
                SubmitTransactionResponse response = server.submitTransaction(allowAstroDollars);
                Log.d(TAG, "createTrust: " + response);
            }
            catch (Exception e){
                Log.d(TAG, "createTrust: Something went wrong!");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public String getBalance(String publicKey) throws IOException {
        Server server = new Server(serverUrl);
        AccountResponse account = server.accounts().account(publicKey);
        return account.getBalances()[0].getBalance();


    }
}
