package com.example.suitapp.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.dummy.DummyContent.DummyItem;
import com.example.suitapp.model.Article;

import java.util.List;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder> {

    private final List<Article> mValues;
    OnArticleListener onArticleListener;

    public ArticleRecyclerViewAdapter(List<Article> items, OnArticleListener onArticleListener) {
        mValues = items;
        this.onArticleListener = onArticleListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_article, parent, false);
        return new ViewHolder(view, onArticleListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tvId.setText(String.valueOf(mValues.get(position).getId()));
        holder.tvName.setText(mValues.get(position).getName());
        holder.tvPrice.setText("$ " + mValues.get(position).getPrice());
        if(mValues.get(position).getColors()>0) {
            holder.tvColor.setText(mValues.get(position).getColors() + " colores");
            holder.tvColor.setVisibility(View.VISIBLE);
        }
        else
            holder.tvColor.setVisibility(View.GONE);
        holder.ivArticle.setImageResource(mValues.get(position).getImage());
        holder.ivArticle.setClipToOutline(true);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvId, tvName, tvPrice, tvColor;
        public final ImageView ivArticle;
        public Article mItem;
        OnArticleListener onArticleListener;

        public ViewHolder(View view, OnArticleListener onArticleListener) {
            super(view);
            mView = view;
            this.tvId = (TextView) view.findViewById(R.id.tvId);
            this.tvName = (TextView) view.findViewById(R.id.tvName);
            this.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            this.tvColor = (TextView) view.findViewById(R.id.tvColor);
            this.ivArticle = view.findViewById(R.id.ivArticle);
            this.onArticleListener = onArticleListener;
        }

        @Override
        public void onClick(View v) {
            onArticleListener.onArticleClick(getAdapterPosition());
        }
    }

    public interface OnArticleListener{
        void onArticleClick(int position);
    }
}