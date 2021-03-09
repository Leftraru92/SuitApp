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
import com.example.suitapp.dummy.DummyContent.DummyItem;
import com.example.suitapp.model.Article;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private List<Article> mValues;
    private OnFavListener onFavListener;


    public FavAdapter(List<Article> items, OnFavListener onFavListener) {
        mValues = items;
        this.onFavListener = onFavListener;
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

        if (mValues.get(position).getArticleImage() != null) {
            byte[] decodedString = Base64.decode(mValues.get(position).getArticleImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.ivArticle.setImageBitmap(decodedByte);
        } else
            holder.ivArticle.setImageResource(R.drawable.ic_emoji_picture);
        holder.ivArticle.setClipToOutline(true);
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;
        else
            return mValues.size();
    }

    public void setItems(List<Article> articleList) {
        this.mValues = articleList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvPrice, tvArticleName;
        public final ImageView ivArticle;
        public Article mItem;
        public Button btDelete;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvArticleName = (TextView) view.findViewById(R.id.tvArticleName);
            ivArticle = view.findViewById(R.id.ivArticle);
            btDelete = view.findViewById(R.id.btDelete);

            btDelete.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btDelete)
                onFavListener.onDeleteClick(mValues.get(getAdapterPosition()).getId());
            else
                onFavListener.onFavClick(mValues.get(getAdapterPosition()).getId());
        }
    }

    public interface OnFavListener {
        void onFavClick(int position);
        void onDeleteClick(int position);
    }
}