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
import com.example.suitapp.model.Article;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FavsRecyclerViewAdapter extends RecyclerView.Adapter<FavsRecyclerViewAdapter.ViewHolder> {

    private final List<Article> mValues;
    private OnFavListener onFavListener;
    private OnAddToCartListener onAddToCartListener;

    public FavsRecyclerViewAdapter(List<Article> items, OnFavListener onFavListener, OnAddToCartListener onAddToCartListener) {
        mValues = items;
        this.onFavListener = onFavListener;
        this.onAddToCartListener = onAddToCartListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_fav, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tvPrice.setText(mValues.get(position).getPriceFormated());
        holder.tvArticleName.setText(mValues.get(position).getName());
        holder.ivArticle.setImageResource(mValues.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvPrice, tvArticleName;
        public final ImageView ivArticle;
        public Article mItem;
        public Button btAddToCart;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvArticleName = (TextView) view.findViewById(R.id.tvArticleName);
            ivArticle = view.findViewById(R.id.ivArticle);
            btAddToCart = view.findViewById(R.id.btAddToCart);

            btAddToCart.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btAddToCart)
                onAddToCartListener.OnAddToCartClick(getAdapterPosition());
            else
                onFavListener.onFavClick(getAdapterPosition());
        }
    }

    public interface OnFavListener {
        void onFavClick(int position);
    }

    public interface OnAddToCartListener {
        void OnAddToCartClick(int position);
    }
}