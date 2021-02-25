package com.example.suitapp.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.dummy.DummyContent.DummyItem;
import com.example.suitapp.model.Store;

import java.util.List;

public class StoresRecyclerViewAdapter extends RecyclerView.Adapter<StoresRecyclerViewAdapter.ViewHolder> {

    private final List<Store> mValues;
    private int card;
    OnStoreListener onStoreListener;

    public StoresRecyclerViewAdapter(List<Store> items, int card, OnStoreListener onStoreListener) {
        mValues = items;
        this.card = card;
        this.onStoreListener = onStoreListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(card, parent, false);
        return new ViewHolder(view, onStoreListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.tvId.setText(String.valueOf(mValues.get(position).getId()));
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
        public Button btEditStore, btDeleteStore;
        public Store mItem;
        OnStoreListener onStoreListener;

        public ViewHolder(View view, OnStoreListener onStoreListener) {
            super(view);
            mView = view;
            tvId = (TextView) view.findViewById(R.id.tvId);
            tvName = (TextView) view.findViewById(R.id.tvName);
            ivStore = view.findViewById(R.id.ivStore);
            btEditStore = view.findViewById(R.id.btEditStore);
            btDeleteStore = view.findViewById(R.id.btDeleteStore);
            this.onStoreListener = onStoreListener;

            view.setOnClickListener(this);
            if (btDeleteStore != null)
                btDeleteStore.setOnClickListener(this);
            if (btEditStore != null)
                btEditStore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btEditStore:
                    onStoreListener.onStoreEdit(getAdapterPosition());
                    break;
                case R.id.btDeleteStore:
                    onStoreListener.onStoreDelete(getAdapterPosition());
                    break;
                default:
                    onStoreListener.onStoreClick(getAdapterPosition());
                    break;
            }

        }
    }

    public interface OnStoreListener {
        void onStoreClick(int position);

        void onStoreEdit(int position);

        void onStoreDelete(int position);
    }

}