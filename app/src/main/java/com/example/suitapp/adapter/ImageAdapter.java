package com.example.suitapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    private List<Integer> mImageIds = new ArrayList<>();
    Context context;

    public ImageAdapter(List<Integer> imagenes){
        this.mImageIds = imagenes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_article_images, parent, false);
        context = parent.getContext();
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(mImageIds.get(position));
        holder.tvQty.setText(String.valueOf(position + 1) + " / " + getItemCount());

    }

    @Override
    public int getItemCount() {
        return mImageIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.myimage);
            tvQty = itemView.findViewById(R.id.tvQty);
        }
    }
}

