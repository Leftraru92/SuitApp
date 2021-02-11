package com.example.suitapp.fragment;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.suitapp.R;
import com.example.suitapp.adapter.ArticleAdapter;
import com.example.suitapp.adapter.ArticlesGroupAdapter;
import com.example.suitapp.dummy.DummyArticles;
import com.example.suitapp.dummy.DummyArticlesGroup;

public class ArticleAddedFragment extends Fragment implements ArticlesGroupAdapter.OnGroupListener {


    public ArticleAddedFragment() {
        // Required empty public constructor
    }

    public static ArticleAddedFragment newInstance(String param1, String param2) {
        ArticleAddedFragment fragment = new ArticleAddedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_article_added, container, false);

        RecyclerView recyclerGroups = root.findViewById(R.id.article_groups_list);
        recyclerGroups.setAdapter(new ArticlesGroupAdapter(DummyArticlesGroup.ITEMS, this, root, R.layout.card_article_group_horizontal_scroll));

        Button btSeeCart = root.findViewById(R.id.btSeeCart);
        btSeeCart.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_nav_article_added_to_nav_cart);
        });
        return root;
    }

    @Override
    public void onGroupClick(int position) {

    }
}