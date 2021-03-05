package com.example.suitapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.model.Variant;
import com.example.suitapp.util.Constants;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class VariantsAdapter extends RecyclerView.Adapter<VariantsAdapter.ViewHolder> {

    private final List<Variant> mValues;
    private final OnVariantListener onVariantListener;
    private final int mCard;

    public VariantsAdapter(List<Variant> items, OnVariantListener onVariantListener, int card) {
        mValues = items;
        this.onVariantListener = onVariantListener;
        this.mCard = card;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(mCard, parent, false);
        return new ViewHolder(view, onVariantListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvSize.setText(mValues.get(position).getSize().getName());
        holder.tvColor.setText(mValues.get(position).getColor().getName());
        if(mValues.get(position).getColor().getHex() != null && !mValues.get(position).getColor().getHex().equals(""))
        holder.ivColor.setColorFilter(Color.parseColor(mValues.get(position).getColor().getHex()));
        holder.tvStock.setText(String.valueOf(mValues.get(position).getStock()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvSize, tvColor, tvStock;
        public final ImageView ivColor;
        public final Button btDelete;
        OnVariantListener onVariantListener;

        public ViewHolder(View view, OnVariantListener onVariantListener) {
            super(view);
            mView = view;
            tvSize = (TextView) view.findViewById(R.id.tvSize);
            tvColor = (TextView) view.findViewById(R.id.tvColor);
            tvStock = view.findViewById(R.id.tvStock);
            btDelete = view.findViewById(R.id.btDelete);
            ivColor = view.findViewById(R.id.ivColor);
            this.onVariantListener = onVariantListener;

            if(onVariantListener != null) {
                btDelete.setOnClickListener(this);
                mView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btDelete)
                onVariantListener.onVariantClick(getAdapterPosition(), Constants.ACTION.DELETE);
            else
                onVariantListener.onVariantClick(getAdapterPosition(), Constants.ACTION.UPDATE);
        }
    }

    public interface OnVariantListener{
        void onVariantClick(int adapterPosition, Constants.ACTION action);
    }

}