package com.example.suitapp.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.suitapp.Constants;
import com.example.suitapp.R;
import com.example.suitapp.viewmodel.SearchViewModel;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        root = inflater.inflate(R.layout.fragment_search, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        setHasOptionsMenu(true);
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
}