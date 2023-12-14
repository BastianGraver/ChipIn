package com.example.payapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.graphics.Color;
import nl.dionsegijn.konfetti.Confetti;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.ParticleSystem;
import nl.dionsegijn.konfetti.emitters.StreamEmitter;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payapp.Fragments.StoreItemAdapter;
import com.example.payapp.MainActivity2;
import com.example.payapp.R;
import com.example.payapp.StellarMaster;
import com.example.payapp.database.AppDatabase;
import com.example.payapp.database.Receipt;
import com.example.payapp.database.StoreItem;
import com.example.payapp.database.StoreItemDao;
import com.example.payapp.database.User;

import java.io.IOException;
import java.util.List;

public class StoreFragment extends Fragment {

    private RecyclerView recyclerView;
    private StoreItemAdapter adapter;
    private AppDatabase database;
    StoreItemDao storeItemDao;

    private String distWalletId = "GDGGD6K47JPRFHLBWVX3XDEGOX55FKVMZQTNKX7O4TDIRMDHFTPIOX43";
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View view = inflater.inflate(R.layout.fragment1, container, false);

        database = AppDatabase.getInstance(requireContext());
        storeItemDao = database.storeItemDao();

        // Retrieve the user object from the arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
            if (user != null) {
                // Now you have the user object in the StoreFragment
                Log.d("Bastian", "onCreateView: User in StoreFragment - " + user.public_seed);
            }
        }


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StoreItemAdapter();
        recyclerView.setAdapter(adapter);


        // Assuming you have access to your Room database and Dao
        List<StoreItem> storeItems = storeItemDao.getAllItems();
        adapter.setStoreItems(storeItems);
        Log.d("Bastian", "onCreateView: From fragment1");

        // Set item click listener in the adapter
        adapter.setOnItemClickListener(storeItem -> showBuyDialog(storeItem));

        return view;
    }

    // Method to show the buy dialog
    private void showBuyDialog(StoreItem storeItem) {
        Log.d("Bastian", "showBuyDialog: init");
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Purchase");
        builder.setMessage("Do you want to buy " + storeItem.itemName + " for " + storeItem.price + " Tokens?");
        Log.d("Bastian", "showBuyDialog: public" + user.public_seed + " private" + user.private_seed);

        builder.setPositiveButton("Buy", (dialog, which) -> {
            try {
                new StellarMaster().sendTokens((float) storeItem.price, user.private_seed, distWalletId, "Item: " + storeItem.itemName);
            } catch (Exception e) {
                Log.d("Bastian", "showBuyDialog: Error! " + e.getMessage());
            }
            Log.d("Bastian", "Item purchased: " + storeItem.itemName);

            // Creates a new receipt.
            Receipt receipt = new Receipt();
            receipt.itemName = storeItem.itemName;
            receipt.itemPrice = storeItem.price;
            receipt.userName = user.name;


            database.receiptDao().insertReceipt(receipt);
            Log.d("Bastian", "showBuyDialog: Receipt addded" + receipt.formattedDateTime);

            // Instead of rootView, use the MainActivity2 view
            MainActivity2 mainActivity = (MainActivity2) requireActivity();
            addConfetti(requireContext(), (ViewGroup) mainActivity.getWindow().getDecorView().getRootView());


            try {
                mainActivity.updateSaldoText("Saldo: " + new StellarMaster().getBalance(user.public_seed) + " Tokens");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.create().show();
    }

    // Method to add confetti to the view
    // Method to add confetti to the view
    private void addConfetti(Context context, ViewGroup rootView) {
        // Create a KonfettiView and add it to the root view of the activity
        KonfettiView konfettiView = new KonfettiView(context);
        rootView.addView(konfettiView);

        // Configure the confetti
        new ParticleSystem(konfettiView)
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setSpeed(1)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, rootView.getWidth() + 50f, -50f, -50f);


        // Remove the KonfettiView after a delay
        konfettiView.postDelayed(() -> rootView.removeView(konfettiView), 5000);
    }

    public interface PurchaseListener {
        void onPurchaseMade();
    }


}


