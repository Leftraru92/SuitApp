package com.example.suitapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.dummy.DummyContent.DummyItem;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Genre;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GenresRecyclerViewAdapter extends RecyclerView.Adapter<GenresRecyclerViewAdapter.ViewHolder> {

    private final List<Genre> mValues;
    private OnGenreListener onGenreListener;

    public GenresRecyclerViewAdapter(List<Genre> items, OnGenreListener onGenreListener) {
        mValues = items;
        this.onGenreListener = onGenreListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_genres, parent, false);
        return new ViewHolder(view, onGenreListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tvId.setText(String.valueOf(mValues.get(position).getId()));
        holder.tvName.setText(mValues.get(position).getName());
        holder.ivGenre.setImageResource(mValues.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvId, tvName;
        public final ImageView ivGenre;
        public Genre mItem;
        OnGenreListener onGenreListener;

        public ViewHolder(View view, OnGenreListener onGenreListener) {
            super(view);
            mView = view;
            tvId = (TextView) view.findViewById(R.id.tvId);
            tvName = (TextView) view.findViewById(R.id.tvName);
            ivGenre = view.findViewById(R.id.ivGenre);
            this.onGenreListener = onGenreListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGenreListener.onGenreClick(getAdapterPosition());
        }
    }

    public interface OnGenreListener{
        void onGenreClick(int position);
    }
}