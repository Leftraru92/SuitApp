package com.example.suitapp.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.model.StoreGroup;

import java.util.List;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class StoreGroupAdapter extends RecyclerView.Adapter<StoreGroupAdapter.ViewHolder> implements StoresAdapter.OnStoreListener {

    private List<StoreGroup> storeGroups;
    View root;

    public StoreGroupAdapter(List<StoreGroup> items, View root) {
        storeGroups = items;
        this.root = root;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_store_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvTitle.setText(storeGroups.get(position).getTitle());
        holder.recyclerArticle.setAdapter(new StoresAdapter(storeGroups.get(position).getStoreList(), R.layout.card_store_circle, this));
    }

    @Override
    public int getItemCount() {
        if (storeGroups == null)
            return 0;
        else
            return storeGroups.size();
    }

    public void setItems(List<StoreGroup> storeGroups) {
        this.storeGroups = storeGroups;
    }


    @Override
    public void onStoreClick(int storeId) {
        Bundle bundle = new Bundle();
        bundle.putInt("storeId", storeId);
        Navigation.findNavController(root).navigate(R.id.action_move_to_articles, bundle);
    }

    @Override
    public void onStoreEdit(int position) {

    }

    @Override
    public void onStoreDelete(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tvId, tvTitle;
        public final RecyclerView recyclerArticle;
        public Button btMore;
        OnGroupListener onGroupListener;

        public ViewHolder(View view) {
            super(view);

            this.tvId = view.findViewById(R.id.tvId);
            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.recyclerArticle = view.findViewById(R.id.recyclerArticle);
            this.btMore = view.findViewById(R.id.btMore);
            this.onGroupListener = onGroupListener;
            if (btMore != null)
                btMore.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onGroupListener.onGroupClick(getAdapterPosition());
        }
    }

    public interface OnGroupListener {
        void onGroupClick(int position);
    }
}
