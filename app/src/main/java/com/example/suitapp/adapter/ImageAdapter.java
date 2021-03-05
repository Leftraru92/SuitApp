package com.example.suitapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
    private List<String> mImageIds;
    Context context;
    OnImageSelectListener onImageSelectListener;

    public ImageAdapter(List<String> imagenes, OnImageSelectListener onImageSelectListener){
        this.mImageIds = imagenes;
        this.onImageSelectListener = onImageSelectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_article_images, parent, false);
        context = parent.getContext();
        return new ImageAdapter.ViewHolder(view, onImageSelectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.imageView.setImageResource(mImageIds.get(position));
        if(mImageIds.get(position) != null) {
            byte[] decodedString = Base64.decode(mImageIds.get(position), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(decodedByte);
        }else
            holder.imageView.setImageResource(R.drawable.ic_emoji_picture);
       // holder.imageView.setClipToOutline(true);
        holder.tvQty.setText(String.valueOf(position + 1) + " / " + getItemCount());

    }

    @Override
    public int getItemCount() {
        return mImageIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView tvQty;
        OnImageSelectListener onImageSelectListener;

        public ViewHolder(@NonNull View itemView, OnImageSelectListener onImageSelectListener) {
            super(itemView);
            this.onImageSelectListener = onImageSelectListener;
            imageView = itemView.findViewById(R.id.myimage);
            tvQty = itemView.findViewById(R.id.tvQty);

            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onImageSelectListener.onImageClick(getAdapterPosition());
        }
    }

    public interface OnImageSelectListener{
        void onImageClick(int position);
    }
}

