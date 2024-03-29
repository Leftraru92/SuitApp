package com.example.suitapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suitapp.R;
import com.example.suitapp.model.PremiumStore;
import com.example.suitapp.model.Store;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class PremiumStoreAdapter extends RecyclerView.Adapter<PremiumStoreAdapter.ViewHolder>{
    private List<Store> premiumStores;
    private LayoutInflater mInflater;
    OnPremiumStoreListener onPremiumStoreListener;

    public PremiumStoreAdapter(List<Store> premiumStores, OnPremiumStoreListener onPremiumStoreListener) {
        this.premiumStores = premiumStores;
        this.onPremiumStoreListener = onPremiumStoreListener;
    }

    @NonNull
    @Override
    public PremiumStoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_premium_store, parent, false);
        return new ViewHolder(view, onPremiumStoreListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PremiumStoreAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(premiumStores.get(position).getName());

        if(premiumStores.get(position).getStoreCoverPhoto() != null && !premiumStores.get(position).getStoreCoverPhoto().equals("")) {
            byte[] decodedString = Base64.decode(premiumStores.get(position).getStoreCoverPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.myimage.setImageBitmap(decodedByte);
        }
        holder.myimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public int getItemCount() {
        return premiumStores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        ImageView myimage;
        RelativeLayout relativeLayout;
        OnPremiumStoreListener onPremiumStoreListener;

        public ViewHolder(@NonNull View view, OnPremiumStoreListener onPremiumStoreListener) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            myimage = view.findViewById(R.id.myimage);
            relativeLayout = view.findViewById(R.id.container);
            this.onPremiumStoreListener = onPremiumStoreListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPremiumStoreListener.onPremiumStoreClick(premiumStores.get(getAdapterPosition()).getId());
        }
    }

    public interface OnPremiumStoreListener{
        void onPremiumStoreClick(int position);
    }
}
