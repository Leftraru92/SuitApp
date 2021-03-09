package com.example.suitapp.adapter;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.model.Article;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Article> mValues;
    private Context context;
    AccessDataDb accessDataDb;
    OnCartListener onDeleteListener;

    public CartAdapter(List<Article> items, Context context, OnCartListener onDeleteListener) {
        mValues = items;
        this.context = context;
        accessDataDb = AccessDataDb.getInstance(context);
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cart, parent, false);
        return new ViewHolder(view, onDeleteListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String color = accessDataDb.getColor(mValues.get(position).getColorId());
        String size = accessDataDb.getSize(mValues.get(position).getSizeId());

        holder.mItem = mValues.get(position);
        String variant = "Color: " + color + ", Talle: " + size;
        holder.tvArticleVariant.setText(variant);
        holder.tvArticleName.setText(mValues.get(position).getName());
        if (onDeleteListener == null)
            holder.tvPrice.setText(mValues.get(position).getPriceFormated());
        else
            holder.tvPrice.setText(mValues.get(position).getPriceFormated(mValues.get(position).getQuantity()));
        holder.tvQuantity.setText(String.valueOf(mValues.get(position).getQuantity()));

        if (mValues.get(position).getArticleImage() != null) {
            byte[] decodedString = Base64.decode(mValues.get(position).getArticleImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.ivArticle.setImageBitmap(decodedByte);
        } else
            holder.ivArticle.setImageResource(R.drawable.ic_emoji_picture);
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;
        else
            return mValues.size();
    }

    public void setItems(List<Article> articleList) {
        mValues = articleList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvArticleName, tvArticleVariant, tvQuantity, tvPrice;
        public final ImageView ivArticle;
        public Article mItem;
        Button btDelete, btMore;
        ConstraintLayout ccBottom;
        OnCartListener onDeleteListener;

        public ViewHolder(View view, OnCartListener onDeleteListener) {
            super(view);
            mView = view;
            tvArticleName = (TextView) view.findViewById(R.id.tvArticleName);
            tvArticleVariant = (TextView) view.findViewById(R.id.tvArticleVariant);
            tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            ivArticle = view.findViewById(R.id.ivArticle);
            btDelete = view.findViewById(R.id.btDelete);
            btMore = view.findViewById(R.id.btMore);
            ccBottom = view.findViewById(R.id.ccBottom);
            this.onDeleteListener = onDeleteListener;

            if (onDeleteListener != null) {
                btDelete.setOnClickListener(this);
                btMore.setOnClickListener(this);
                view.setElevation(6);
            } else {
                ccBottom.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == btDelete.getId())
                onDeleteListener.onDeleteClick(mValues.get(getAdapterPosition()));
            else
                onDeleteListener.onMoreClick(mValues.get(getAdapterPosition()).getStoreId());
        }
    }

    public interface OnCartListener {
        void onDeleteClick(Article articleId);

        void onMoreClick(int storeId);
    }
}