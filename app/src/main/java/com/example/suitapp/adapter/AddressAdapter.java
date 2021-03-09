package com.example.suitapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.model.Address;
import com.example.suitapp.model.Article;
import com.example.suitapp.model.ProvinceList;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<Address> mValues;
    OnAddressListener onAddressListener;
    int id;

    public AddressAdapter(List<Address> items, OnAddressListener onAddressListener, int id) {
        mValues = items;
        this.onAddressListener = onAddressListener;
        this.id = id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_address, parent, false);
        return new ViewHolder(view, onAddressListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.lbAddress.setText(mValues.get(position).getAddress());
        holder.tvAddress.setText(mValues.get(position).getAddressDetail());

        if (mValues.get(position).getId() == id)
            holder.ivCheck.setVisibility(View.VISIBLE);
        else
            holder.ivCheck.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;
        else
            return mValues.size();
    }

    public void setItems(List<Address> articleList) {
        mValues = articleList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView lbAddress, tvAddress;
        public final Button btDelete;
        public final ImageView ivCheck;
        OnAddressListener onAddressListener;

        public ViewHolder(View view, OnAddressListener onAddressListener) {
            super(view);
            mView = view;
            lbAddress = (TextView) view.findViewById(R.id.lbAddress);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            ivCheck = view.findViewById(R.id.ivCheck);
            btDelete = view.findViewById(R.id.btDelete);
            this.onAddressListener = onAddressListener;

            if (onAddressListener != null) {
                btDelete.setOnClickListener(this);
                mView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == btDelete.getId())
                onAddressListener.onDeleteClick(mValues.get(getAdapterPosition()).getId());
            else
                onAddressListener.onEditClick(mValues.get(getAdapterPosition()).getId());
        }
    }

    public interface OnAddressListener {
        void onDeleteClick(int AddressId);

        void onEditClick(int storeId);
    }
}