package com.example.payapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payapp.Fragments.ReceiptAdapter;
import com.example.payapp.R;
import com.example.payapp.database.AppDatabase;
import com.example.payapp.database.Receipt;
import com.example.payapp.database.ReceiptDao;
import com.example.payapp.database.User;

import java.util.List;

public class ReceiptFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReceiptAdapter adapter;
    private AppDatabase database;
    ReceiptDao receiptDao;

    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
            if (user != null) {
                // Now you have the user object in the StoreFragment
                Log.d("Bastian", "onCreateView: User in StoreFragment - " + user.public_seed);
            }
        }

        database = AppDatabase.getInstance(requireContext());
        receiptDao = database.receiptDao();
        Log.d("Bastian", "onCreateView: From ReceiptFragment");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReceiptAdapter();
        recyclerView.setAdapter(adapter);
        Log.d("Bastian", "onCreateView: From ReceiptFragment");

        // Load receipt data from the database
        try {
            List<Receipt> receipts = receiptDao.getReceiptsByUserName(user.name);
            Log.d("Bastian", "onCreateView: " + receiptDao.getAllReceipts().toString());
            adapter.setReceipts(receipts);
        }
        catch (Exception e){
            Log.d("Bastian", "onCreateView: " + e.getMessage());
        }

        return view;
    }
}