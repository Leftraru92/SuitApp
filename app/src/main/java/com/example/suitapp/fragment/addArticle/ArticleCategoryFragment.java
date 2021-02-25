package com.example.suitapp.fragment.addArticle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suitapp.R;
import com.example.suitapp.adapter.CategoriesAdapter;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Item;
import com.example.suitapp.viewmodel.AddArticleViewModel;

import java.util.List;

public class ArticleCategoryFragment extends Fragment implements CategoriesAdapter.OnCategoryListener {
    View root;
    private AddArticleViewModel mViewModel;
    boolean fromReview;
    private List<Item> categories;
    private CategoriesAdapter categoriesAdapter;

    public static ArticleCategoryFragment newInstance() {
        return new ArticleCategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_article_category, container, false);
        init();
        getData();
        return root;
    }

    private void init() {
        //set
        mViewModel = new ViewModelProvider(getActivity()).get(AddArticleViewModel.class);
        fromReview = false;
        if (getArguments() != null)
            fromReview = ArticleNameFragmentArgs.fromBundle(getArguments()).getFromReview();

        //bind
        categoriesAdapter = new CategoriesAdapter(null, this);
        RecyclerView listCategory = root.findViewById(R.id.listCategory);
        listCategory.setAdapter(categoriesAdapter);

    }

    private void getData() {
        AccessDataDb accessDataDba = AccessDataDb.getInstance(getContext());

        categories = accessDataDba.getCategories();
        categoriesAdapter.setItems(categories);
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryClick(int position) {
        mViewModel.setCategory((Category) categories.get(position));

        if (fromReview)
            Navigation.findNavController(root).navigate(R.id.action_nav_article_category_to_nav_article_review);
        else
            Navigation.findNavController(root).navigate(R.id.action_nav_article_category_to_nav_article_genre);
    }
}