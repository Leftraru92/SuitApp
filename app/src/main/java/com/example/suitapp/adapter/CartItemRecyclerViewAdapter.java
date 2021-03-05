package com.example.suitapp.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.dummy.DummyContent.DummyItem;
import com.example.suitapp.model.Article;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CartItemRecyclerViewAdapter extends RecyclerView.Adapter<CartItemRecyclerViewAdapter.ViewHolder> {

    private final List<Article> mValues;

    public CartItemRecyclerViewAdapter(List<Article> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tvArticleVariant.setText("Color: Negro, Talle: M");
        holder.tvArticleName.setText(mValues.get(position).getName());
        holder.tvPrice.setText(mValues.get(position).getPriceFormated());
        holder.tvQuantity.setText("1");
        holder.ivArticle.setImageResource(mValues.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvArticleName, tvArticleVariant, tvQuantity, tvPrice;
        public final ImageView ivArticle;
        public Article mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvArticleName = (TextView) view.findViewById(R.id.tvArticleName);
            tvArticleVariant = (TextView) view.findViewById(R.id.tvArticleVariant);
            tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            ivArticle = view.findViewById(R.id.ivArticle);
        }
    }
}