package com.example.suitapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.model.Gender;
import com.example.suitapp.util.Util;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.ViewHolder> {

    private List<Gender> mValues;
    private OnGenreListener onGenreListener;
    private Context context;

    public GenderAdapter(List<Gender> items, OnGenreListener onGenreListener, Context context) {
        mValues = items;
        this.onGenreListener = onGenreListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_genres, parent, false);

        ViewGroup.LayoutParams params =  view.getLayoutParams();
        params.width = (parent.getMeasuredWidth() / 3) - Util.getPxByDensity(context.getResources(), 6);
        view.setLayoutParams(params);

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
        if (mValues == null)
            return 0;
        else
            return mValues.size();
    }

    public void setItems(List<Gender> genderList) {
        mValues = genderList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView tvId, tvName;
        public final ImageView ivGenre;
        public Gender mItem;
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

    public interface OnGenreListener {
        void onGenreClick(int position);
    }
}