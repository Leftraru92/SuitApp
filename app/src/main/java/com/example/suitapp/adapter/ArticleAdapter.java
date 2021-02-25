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
import com.example.suitapp.fragment.ArticleFragment;
import com.example.suitapp.model.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> mValues;
    OnArticleListener onArticleListener;
    private int card;
    private boolean isOwner;

    public ArticleAdapter(List<Article> items, OnArticleListener onArticleListener, int card) {
        mValues = items;
        this.onArticleListener = onArticleListener;
        this.card = card;
        this.isOwner = false;
    }

    public ArticleAdapter(List<Article> items, OnArticleListener onArticleListener, int card, boolean isOwner) {
        mValues = items;
        this.onArticleListener = onArticleListener;
        this.card = card;
        this.isOwner = isOwner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(card, parent, false);
        return new ViewHolder(view, onArticleListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.tvId.setText(String.valueOf(mValues.get(position).getId()));
        holder.tvName.setText(mValues.get(position).getName());
        holder.tvPrice.setText("$ " + mValues.get(position).getPrice());

        if (holder.tvColor != null) {
            if (mValues.get(position).getColors() > 0) {
                holder.tvColor.setText(mValues.get(position).getColors() + " colores");
                holder.tvColor.setVisibility(View.VISIBLE);
            } else
                holder.tvColor.setVisibility(View.GONE);
        }
        if(mValues.get(position).getArticleImage() != null) {
            byte[] decodedString = Base64.decode(mValues.get(position).getArticleImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.ivArticle.setImageBitmap(decodedByte);
        }else
            holder.ivArticle.setImageResource(R.drawable.ic_emoji_picture);
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
        public final Button ibOpciones;
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
            this.ibOpciones = view.findViewById(R.id.ibOpciones);
            this.onArticleListener = onArticleListener;

            if (ibOpciones != null) {
                ibOpciones.setVisibility((isOwner) ? View.VISIBLE : View.GONE);
                ibOpciones.setOnClickListener(this);
            }

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (ibOpciones != null && v.getId() == ibOpciones.getId())
                onArticleListener.onArticleEditClick(getAdapterPosition(), v);
            else
                onArticleListener.onArticleClick(getAdapterPosition());
        }
    }

    public interface OnArticleListener {
        void onArticleClick(int position);
        void onArticleEditClick(int position, View v);
    }
}