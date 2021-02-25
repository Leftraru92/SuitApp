package com.example.suitapp.fragment.addArticle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suitapp.R;
import com.example.suitapp.adapter.CategoriesAdapter;
import com.example.suitapp.adapter.GenderAdapter;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.model.Gender;
import com.example.suitapp.viewmodel.AddArticleViewModel;

import java.util.List;

public class ArticleGenreFragment extends Fragment implements GenderAdapter.OnGenreListener {
    View root;
    private AddArticleViewModel mViewModel;
    boolean fromReview;
    private List<Gender> genderList;
    private GenderAdapter genderAdapter;

    public static ArticleGenreFragment newInstance() {
        return new ArticleGenreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_article_genre, container, false);
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
        RecyclerView listGenres = root.findViewById(R.id.listGenres);
        genderAdapter = new GenderAdapter(null, this, getContext());
        listGenres.setAdapter(genderAdapter);
    }

    private void getData() {
        AccessDataDb accessDataDba = AccessDataDb.getInstance(getContext());

        genderList = accessDataDba.getGenders();
        genderAdapter.setItems(genderList);
        genderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGenreClick(int position) {
        mViewModel.setGenre((Gender) genderList.get(position));

        if (fromReview)
            Navigation.findNavController(root).navigate(R.id.action_nav_article_genre_to_nav_article_review);
        else
            Navigation.findNavController(root).navigate(R.id.action_nav_article_genre_to_nav_article_variants);
    }
}