package com.example.suitapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.model.ShippingPrice;
import com.example.suitapp.util.Constants;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ShippingPriceAdapter extends RecyclerView.Adapter<ShippingPriceAdapter.ViewHolder> {

    private final List<ShippingPrice> mValues;
    private final OnShippingPriceListener onShippingPriceListener;
    private final int card;

    public ShippingPriceAdapter(List<ShippingPrice> items, OnShippingPriceListener onShippingPriceListener, int card) {
        mValues = items;
        this.onShippingPriceListener = onShippingPriceListener;
        this.card = card;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(card, parent, false);
        return new ViewHolder(view, onShippingPriceListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvProvince.setText(String.valueOf(mValues.get(position).getProvince().getName()));
        holder.tvPrice.setText(mValues.get(position).getPriceFormatted());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvProvince;
        public final TextView tvPrice;
        public final Button btDelete;
        OnShippingPriceListener onShippingPriceListener;

        public ViewHolder(View view, OnShippingPriceListener onShippingPriceListener) {
            super(view);
            mView = view;
            tvProvince = (TextView) view.findViewById(R.id.tvProvince);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            btDelete = view.findViewById(R.id.btDelete);
            this.onShippingPriceListener = onShippingPriceListener;

            if(onShippingPriceListener != null) {
                btDelete.setOnClickListener(this);
                mView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btDelete)
                onShippingPriceListener.onShippingPriceClick(getAdapterPosition(), Constants.ACTION.DELETE);
            else
                onShippingPriceListener.onShippingPriceClick(getAdapterPosition(), Constants.ACTION.UPDATE);
        }
    }

    public interface OnShippingPriceListener{
        void onShippingPriceClick(int adapterPosition, Constants.ACTION action);
    }

}