package com.example.suitapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.suitapp.adapter.CategoriesAdapter;
import com.example.suitapp.R;
import com.example.suitapp.adapter.GenderAdapter;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Item;
import com.example.suitapp.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements GenderAdapter.OnGenreListener, CategoriesAdapter.OnCategoryListener {
    View root;
    SearchViewModel searchViewModel;
    final int RC_CATEGORIES = 1, RC_GENDERS = 2;
    List<Item> categoryList;
    List<Gender> genderList;
    CategoriesAdapter categoriesAdapter;
    GenderAdapter genresAdapter;
    ProgressBar progressCat, progressGenres;
    TextView tvErrorCat, tvErrorGenres;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_category_list, container, false);
        init();
        getData();
        return root;
    }

    private void init() {
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        categoryList = new ArrayList<>();
        genderList = new ArrayList<>();

        //bind
        RecyclerView recyclerView = root.findViewById(R.id.list);
        RecyclerView recyclerGenres = root.findViewById(R.id.genres_list);
        progressCat = root.findViewById(R.id.progressCat);
        progressGenres = root.findViewById(R.id.progressGenres);
        tvErrorCat = root.findViewById(R.id.tvErrorCat);
        tvErrorGenres = root.findViewById(R.id.tvErrorGenres);

        // Set the adapter
        genresAdapter = new GenderAdapter(null, this, getContext());
        recyclerGenres.setAdapter(genresAdapter);
        categoriesAdapter = new CategoriesAdapter(null, this);
        recyclerView.setAdapter(categoriesAdapter);
    }

    private void getData() {
        AccessDataDb accessDataDba = AccessDataDb.getInstance(getContext());

        genderList = accessDataDba.getGenders();
        genresAdapter.setItems(genderList);
        genresAdapter.notifyDataSetChanged();
        progressGenres.setVisibility(View.GONE);

        categoryList = accessDataDba.getCategories();
        categoriesAdapter.setItems(categoryList);
        categoriesAdapter.notifyDataSetChanged();
        progressCat.setVisibility(View.GONE);

    }

    @Override
    public void onGenreClick(int position) {
        searchViewModel.clean();
        searchViewModel.setGenre(genderList.get(position));
        searchViewModel.setStore(false);
        Navigation.findNavController(root).navigate(R.id.action_nav_categories_to_nav_article);
    }

    @Override
    public void onCategoryClick(int position) {
        searchViewModel.clean();
        searchViewModel.setCategory((Category) categoryList.get(position));
        searchViewModel.setStore(false);
        Navigation.findNavController(root).navigate(R.id.action_nav_categories_to_nav_article);
    }


}