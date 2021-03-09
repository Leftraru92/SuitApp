package com.example.suitapp.adapter;

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
import com.example.suitapp.util.Util;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private List<Article> mValues;
    private Context context;
    AccessDataDb accessDataDb;

    public ShopAdapter(List<Article> items, Context context) {
        mValues = items;
        this.context = context;
        accessDataDb = AccessDataDb.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String color = accessDataDb.getColor(mValues.get(position).getColorId());
        String size = accessDataDb.getSize(mValues.get(position).getSizeId());

        holder.mItem = mValues.get(position);
        String variant = "Color: " + color + ", Talle: " + size;
        holder.tvArticleVariant.setText(variant);
        holder.tvArticleName.setText(mValues.get(position).getName());
        holder.tvPrice.setText(mValues.get(position).getPriceFormated(mValues.get(position).getQuantity()));
        holder.tvQuantity.setText(String.valueOf(mValues.get(position).getQuantity()));
        holder.tvDate.setText(Util.getDateFormatted(mValues.get(position).getBuyDate(), true) );

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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView tvArticleName, tvArticleVariant, tvQuantity, tvPrice, tvDate;
        public final ImageView ivArticle;
        public Article mItem;
        Button btDelete, btMore;
        ConstraintLayout ccBottom;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvArticleName = (TextView) view.findViewById(R.id.tvArticleName);
            tvArticleVariant = (TextView) view.findViewById(R.id.tvArticleVariant);
            tvDate = view.findViewById(R.id.tvDate);
            tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            ivArticle = view.findViewById(R.id.ivArticle);
            btDelete = view.findViewById(R.id.btDelete);
            btMore = view.findViewById(R.id.btMore);
            ccBottom = view.findViewById(R.id.ccBottom);
        }
    }
}