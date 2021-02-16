package com.example.suitapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suitapp.util.Constants;
import com.example.suitapp.adapter.ArticleAdapter;
import com.example.suitapp.R;
import com.example.suitapp.adapter.StoresRecyclerViewAdapter;
import com.example.suitapp.dummy.DummyArticles;
import com.example.suitapp.dummy.DummyStores;
import com.example.suitapp.listener.OclArticlesFilter;
import com.example.suitapp.viewmodel.SearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A fragment representing a list of Items.
 */
public class ArticleFragment extends Fragment implements StoresRecyclerViewAdapter.OnStoreListener, ArticleAdapter.OnArticleListener {

    View root;
    SearchViewModel searchViewModel;
    View store_banner;
    RecyclerView recyclerViewStores;

    public static ArticleFragment newInstance(int columnCount) {
        return new ArticleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        root = inflater.inflate(R.layout.fragment_articles, container, false);

        store_banner = root.findViewById(R.id.store_banner);
        recyclerViewStores = root.findViewById(R.id.listStores);

        searchViewModel.isStore().observe(getViewLifecycleOwner(), s -> updateUi());


        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextInputEditText tiet = toolbar.findViewById(R.id.tietSearch);
        tiet.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_article_to_nav_search));

        Button btFiltrar = getActivity().findViewById(R.id.btFiltrar);
        OclArticlesFilter oclFiltrarMisOrdenes = new OclArticlesFilter(getContext());
        btFiltrar.setOnClickListener(oclFiltrarMisOrdenes);

        //Escucho el buscador
        searchViewModel.getSearchText().

                observe(getViewLifecycleOwner(), s ->

                {
                    Log.d(Constants.LOG, s);
                    tiet.setText(s);
                });

        return root;
    }

    private void updateUi() {
        //modo artículo / modo tienda
        if (searchViewModel.isStore().getValue()) {
            recyclerViewStores.setVisibility(View.GONE);
            store_banner.setVisibility(View.VISIBLE);
            ((TextView) root.findViewById(R.id.tvName)).setText(searchViewModel.getStoreName());
            //TODO: set info banner
        } else {
            store_banner.setVisibility(View.GONE);
            //TODO: get info adapter stores
            recyclerViewStores.setVisibility(View.VISIBLE);
            recyclerViewStores.setAdapter(new StoresRecyclerViewAdapter(DummyStores.ITEMS, R.layout.card_store_circle, this));
        }

        RecyclerView recyclerView = root.findViewById(R.id.list);
        recyclerView.setAdapter(new ArticleAdapter(DummyArticles.ITEMS, this, R.layout.card_article));
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
        searchViewModel.setStoreName(DummyStores.ITEMS.get(position).getName());
        searchViewModel.setStore(true);
        Toast.makeText(getContext(), "Se tocó la tienda " + DummyStores.ITEMS.get(position).getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onArticleClick(int position) {
        Toast.makeText(getContext(), "Se tocó el artículo " + DummyArticles.ITEMS.get(position).getName(), Toast.LENGTH_LONG).show();
        Navigation.findNavController(root).navigate(R.id.action_nav_article_to_nav_article_detail);
    }
}