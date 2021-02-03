package com.example.suitapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.suitapp.Constants;
import com.example.suitapp.adapter.ArticleRecyclerViewAdapter;
import com.example.suitapp.R;
import com.example.suitapp.adapter.StoresRecyclerViewAdapter;
import com.example.suitapp.dummy.DummyArticles;
import com.example.suitapp.dummy.DummyCategories;
import com.example.suitapp.dummy.DummyContent;
import com.example.suitapp.dummy.DummyStores;
import com.example.suitapp.viewmodel.SearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A fragment representing a list of Items.
 */
public class ArticleFragment extends Fragment implements StoresRecyclerViewAdapter.OnStoreListener, ArticleRecyclerViewAdapter.OnArticleListener {

    View root;
    SearchViewModel searchViewModel;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ArticleFragment newInstance(int columnCount) {
        ArticleFragment fragment = new ArticleFragment();
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
        searchViewModel =
                new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        root = inflater.inflate(R.layout.fragment_articles, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.list);
        RecyclerView recyclerViewStores = root.findViewById(R.id.listStores);
       // recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        recyclerView.setAdapter(new ArticleRecyclerViewAdapter(DummyArticles.ITEMS, this));
        recyclerViewStores.setAdapter(new StoresRecyclerViewAdapter(DummyStores.ITEMS, true, this));

    setHasOptionsMenu(true);

    Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    TextInputEditText tiet = toolbar.findViewById(R.id.tietSearch);
        tiet.setOnClickListener(v ->Navigation.findNavController(root).

    navigate(R.id.action_nav_article_to_nav_search));

    //Escucho el buscador
        searchViewModel.getSearchText().

    observe(getViewLifecycleOwner(),s ->

    {
        Log.d(Constants.LOG, s);
        tiet.setText(s);
    });

        return root;
}

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Navigation.findNavController(root).navigate(R.id.action_nav_article_to_nav_cart);
                return true;
            default:
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(root));
        }
    }

    @Override
    public void onStoreClick(int position) {
        Toast.makeText(getContext(), "Se tocó la tienda " + DummyStores.ITEMS.get(position).getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onArticleClick(int position) {
        Toast.makeText(getContext(), "Se tocó el artículo " + DummyArticles.ITEMS.get(position).getName(), Toast.LENGTH_LONG).show();
    }
}