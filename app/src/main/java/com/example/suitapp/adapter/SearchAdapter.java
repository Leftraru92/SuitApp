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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<String> mValues;
    private final OnSeachListener onSeachListener;

    public SearchAdapter(List<String> items, OnSeachListener onSeachListener) {
        mValues = items;
        this.onSeachListener = onSeachListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_previus_search, parent, false);
        return new ViewHolder(view, onSeachListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvText.setText(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;
        else
            return mValues.size();
    }

    public void setItems(List<String> previusSearch) {
        this.mValues = previusSearch;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvText;
        OnSeachListener onSeachListener;

        public ViewHolder(View view, OnSeachListener onSeachListener) {
            super(view);
            mView = view;
            tvText = (TextView) view.findViewById(R.id.tvText);
            this.onSeachListener = onSeachListener;

            if (onSeachListener != null) {
                mView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            onSeachListener.onSearchClick(mValues.get(getAdapterPosition()));
        }
    }

    public interface OnSeachListener {
        void onSearchClick(String value);
    }

}