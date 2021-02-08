package com.example.suitapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.suitapp.adapter.CategorieRecyclerViewAdapter;
import com.example.suitapp.R;
import com.example.suitapp.adapter.GenresRecyclerViewAdapter;
import com.example.suitapp.dummy.DummyCategories;
import com.example.suitapp.dummy.DummyContent;
import com.example.suitapp.dummy.DummyGenres;
import com.example.suitapp.viewmodel.SearchViewModel;

/**
 * A fragment representing a list of Items.
 */
public class CategoryFragment extends Fragment implements GenresRecyclerViewAdapter.OnGenreListener, CategorieRecyclerViewAdapter.OnCategoryListener {
    View root;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    SearchViewModel searchViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CategoryFragment newInstance(int columnCount) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_category_list, container, false);
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);

        RecyclerView recyclerView = root.findViewById(R.id.list);
        RecyclerView recyclerGenres = root.findViewById(R.id.genres_list);

        // Set the adapter
        recyclerView.setAdapter(new CategorieRecyclerViewAdapter(DummyCategories.ITEMS, this));
        recyclerGenres.setAdapter(new GenresRecyclerViewAdapter(DummyGenres.ITEMS, this));

        return root;
    }

    @Override
    public void onGenreClick(int position) {
        Toast.makeText(getContext(), "Toco el género  " + DummyGenres.ITEMS.get(position).getName(), Toast.LENGTH_LONG).show();
        searchViewModel.setStore(false);
        Navigation.findNavController(root).navigate(R.id.action_nav_categories_to_nav_article);
    }

    @Override
    public void onCategoryClick(int position) {
        Toast.makeText(getContext(), "Toco la categoría  " + DummyCategories.ITEMS.get(position).getName(), Toast.LENGTH_LONG).show();
        searchViewModel.setStore(false);
        Navigation.findNavController(root).navigate(R.id.action_nav_categories_to_nav_article);
    }
}