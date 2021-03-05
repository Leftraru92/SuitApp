package com.example.suitapp.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.suitapp.MobileNavigationDirections;
import com.example.suitapp.R;
import com.example.suitapp.fragment.ArticleDetailFragmentDirections;
import com.example.suitapp.fragment.ArticleFragmentDirections;
import com.example.suitapp.model.ArticleGroup;

import java.util.List;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class ArticlesGroupAdapter extends RecyclerView.Adapter<ArticlesGroupAdapter.ViewHolder> implements ArticleAdapter.OnArticleListener {

    private List<ArticleGroup> articleGroups;
    OnGroupListener onGroupListener;
    View root;
    int card;
    int cardArticle;

    public ArticlesGroupAdapter(List<ArticleGroup> items, OnGroupListener onGroupListener, View root, int card) {
        articleGroups = items;
        this.onGroupListener = onGroupListener;
        this.root = root;
        this.card = card;
        this.cardArticle = (card == R.layout.card_article_group) ? R.layout.card_article_2 : R.layout.card_article_horizontal_scroll;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(card, parent, false);
        return new ViewHolder(view, onGroupListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        holder.tvId.setText(String.valueOf(articleGroups.get(position).getId()));
        holder.tvTitle.setText(articleGroups.get(position).getTitle());

        holder.recyclerArticle.setAdapter(new ArticleAdapter(articleGroups.get(position).getArticleList(), this, cardArticle));
    }

    @Override
    public int getItemCount() {
        if (articleGroups == null)
            return 0;
        else
            return articleGroups.size();
    }

    public void setItems(List<ArticleGroup> artcilegroups) {
        this.articleGroups = artcilegroups;
    }

    @Override
    public void onArticleClick(int articleId) {
        Bundle bundle = new Bundle();
        bundle.putInt("articleId", articleId);
        Navigation.findNavController(root).navigate(R.id.action_move_to_article, bundle);

    }

    @Override
    public void onArticleEditClick(int position, View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tvId, tvTitle;
        public final RecyclerView recyclerArticle;
        public Button btMore;
        OnGroupListener onGroupListener;

        public ViewHolder(View view, OnGroupListener onGroupListener) {
            super(view);

            this.tvId = (TextView) view.findViewById(R.id.tvId);
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
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
