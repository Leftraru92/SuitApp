package com.example.suitapp.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.dummy.DummyContent.DummyItem;
import com.example.suitapp.model.Store;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class StoresRecyclerViewAdapter extends RecyclerView.Adapter<StoresRecyclerViewAdapter.ViewHolder> {

    private final List<Store> mValues;
    private boolean circle;
    OnStoreListener onStoreListener;

    public StoresRecyclerViewAdapter(List<Store> items, boolean circle, OnStoreListener onStoreListener) {
        mValues = items;
        this.circle = circle;
        this.onStoreListener = onStoreListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        if (circle)
            layout = R.layout.card_store_circle;
        else
            layout = R.layout.card_stores;

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(view, onStoreListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tvId.setText(String.valueOf(mValues.get(position).getId()));
        holder.tvName.setText(mValues.get(position).getName());
        holder.ivStore.setImageResource(mValues.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvId, tvName;
        public ImageView ivStore;
        public Store mItem;
        OnStoreListener onStoreListener;

        public ViewHolder(View view, OnStoreListener onStoreListener) {
            super(view);
            mView = view;
            tvId = (TextView) view.findViewById(R.id.tvId);
            tvName = (TextView) view.findViewById(R.id.tvName);
            ivStore = view.findViewById(R.id.ivStore);
            this.onStoreListener = onStoreListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onStoreListener.onStoreClick(getAdapterPosition());
        }
    }

    public interface OnStoreListener{
        void onStoreClick(int position);
    }
}