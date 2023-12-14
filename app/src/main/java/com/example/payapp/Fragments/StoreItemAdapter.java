package com.example.payapp.Fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payapp.R;
import com.example.payapp.database.AppDatabase;
import com.example.payapp.database.StoreItemDao;
import com.example.payapp.database.StoreItem;

import java.util.List;

public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.StoreItemViewHolder> {

    private List<StoreItem> storeItems;
    private AppDatabase database;
    private StoreItemDao storeItemDao;

    private OnItemClickListener onItemClickListener;

    // Interface for handling item clicks
    public interface OnItemClickListener {
        void onItemClick(StoreItem storeItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public void setStoreItems(List<StoreItem> storeItems) {
        this.storeItems = storeItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);

        return new StoreItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreItemViewHolder holder, int position) {
        StoreItem storeItem = storeItems.get(position);

        // Set data to views
        holder.textViewName.setText(storeItem.itemName);
        holder.textViewPrice.setText(String.valueOf(storeItem.price + "\n" + "Tokens"));
        // Load image using your preferred image loading library (e.g., Glide or Picasso)
        // For simplicity, we use a placeholder image here.
        holder.imageView.setImageResource(storeItem.image);

        // Set click listener on the item view
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(storeItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeItems != null ? storeItems.size() : 0;
    }

    static class StoreItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewPrice;

        StoreItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}

