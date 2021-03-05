package com.example.suitapp.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitapp.adapter.SearchAdapter;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.database.QueryDbGet;
import com.example.suitapp.database.QueryDbInsert;
import com.example.suitapp.util.Constants;
import com.example.suitapp.R;
import com.example.suitapp.viewmodel.SearchViewModel;

import java.util.List;

public class SearchFragment extends Fragment implements SearchAdapter.OnSeachListener {

    private SearchViewModel searchViewModel;
    SearchAdapter searchAdapter;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        root = inflater.inflate(R.layout.fragment_search, container, false);
        searchAdapter = new SearchAdapter(null, this);
        RecyclerView listPrevSearch = root.findViewById(R.id.listPrevSearch);
        listPrevSearch.setAdapter(searchAdapter);
        setHasOptionsMenu(true);
        getDataFromDb();
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        //muestra el boton expandido
        searchView.setIconified(false);
        //no deja colapsar el botón
        searchView.setOnCloseListener(() -> {
            return true;
        });

        searchView.setQuery(searchViewModel.getSearchText().getValue(), false);

        //Escucho las modificaciones y el enviar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(Constants.LOG, "onQueryTextSubmit " + query);
                searchViewModel.setSearchText(query);
                saveSearchOnDb(query);
                Navigation.findNavController(root).navigate(R.id.action_nav_search_to_nav_article);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText))
                    searchViewModel.setSearchText("");
                return false;
            }
        });

        searchViewModel.getSearchText().observe(getViewLifecycleOwner(), s -> {
            searchView.setQuery(s, false);
        });
    }

    private void getDataFromDb() {
        AccessDataDb accessDataDba = AccessDataDb.getInstance(getContext());
        List<String> previusSearch = accessDataDba.getPreviusSearch();
        searchAdapter.setItems(previusSearch);
    }

    private void saveSearchOnDb(String query) {
        QueryDbInsert.insertSearch(getContext(), query);
    }

    @Override
    public void onSearchClick(String value) {
        searchViewModel.setSearchText(value);
        Navigation.findNavController(root).navigate(R.id.action_nav_search_to_nav_article);
    }
}