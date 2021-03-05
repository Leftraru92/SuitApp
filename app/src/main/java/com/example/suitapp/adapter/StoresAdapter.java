package com.example.suitapp.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.model.Store;

import java.util.List;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.ViewHolder> {

    private List<Store> mValues;
    private int card;
    OnStoreListener onStoreListener;

    public StoresAdapter(List<Store> items, int card, OnStoreListener onStoreListener) {
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

        if (mValues.get(position).getStoreLogo() != null && !mValues.get(position).getStoreLogo().equals("")) {
            byte[] decodedString = Base64.decode(mValues.get(position).getStoreLogo(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.ivStore.setImageBitmap(decodedByte);
        }

        if(holder.ivBanner != null){
            if (mValues.get(position).getStoreCoverPhoto() != null && !mValues.get(position).getStoreCoverPhoto().equals("")) {
                byte[] decodedString = Base64.decode(mValues.get(position).getStoreCoverPhoto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.ivBanner.setImageBitmap(decodedByte);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;
        else
            return mValues.size();
    }

    public void setItems(List<Store> storeList) {
        this.mValues = storeList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvId, tvName;
        public ImageView ivStore, ivBanner;
        public Button btEditStore, btDeleteStore;
        public Store mItem;
        OnStoreListener onStoreListener;

        public ViewHolder(View view, OnStoreListener onStoreListener) {
            super(view);
            mView = view;
            tvId = (TextView) view.findViewById(R.id.tvId);
            tvName = (TextView) view.findViewById(R.id.tvName);
            ivStore = view.findViewById(R.id.ivStore);
            ivBanner = view.findViewById(R.id.ivBanner);
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
                    onStoreListener.onStoreEdit(mValues.get(getAdapterPosition()).getId());
                    break;
                case R.id.btDeleteStore:
                    onStoreListener.onStoreDelete(mValues.get(getAdapterPosition()).getId());
                    break;
                default:
                    onStoreListener.onStoreClick(mValues.get(getAdapterPosition()).getId());
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