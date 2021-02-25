package com.example.suitapp.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.suitapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AddArticleImagesAdapter extends RecyclerView.Adapter<AddArticleImagesAdapter.ViewHolder> {

    private final List<Bitmap> mValues;
    private final OnImageListener onImageListener;
    private final boolean showDelete;

    public AddArticleImagesAdapter(List<Bitmap> items, OnImageListener onImageListener, boolean showDelete) {
        mValues = items;
        this.onImageListener = onImageListener;
        this.showDelete = showDelete;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_add_image, parent, false);
        return new ViewHolder(view, onImageListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ivArticle.setImageBitmap(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final ImageView ivArticle;
        public final FloatingActionButton fabDelete;
        OnImageListener onImageListener;

        public ViewHolder(View view, OnImageListener onImageListener) {
            super(view);
            mView = view;
            this.onImageListener = onImageListener;
            ivArticle = view.findViewById(R.id.ivArticle);
            fabDelete = view.findViewById(R.id.fabDelete);
            //this.onShippingPriceListener = onShippingPriceListener;

            fabDelete.setOnClickListener(this);
            mView.setOnClickListener(this);

            if (showDelete) {
                fabDelete.setVisibility(View.VISIBLE);
            } else
                fabDelete.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fabDelete)
                onImageListener.onImageDelete(getAdapterPosition());
            else
                onImageListener.onImageClick(getAdapterPosition());
        }
    }

    public interface OnImageListener {
        void onImageClick(int position);

        void onImageDelete(int position);
    }

}