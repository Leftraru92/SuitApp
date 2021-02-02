package com.example.suitapp.fragment;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.suitapp.Constants;
import com.example.suitapp.R;
import com.example.suitapp.viewmodel.HomeViewModel;
import com.example.suitapp.viewmodel.SearchViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class HomeFragment extends Fragment {

    View root;
    private HomeViewModel homeViewModel;
    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        searchViewModel =
                new ViewModelProvider(getActivity()).get(SearchViewModel.class);

        root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        //searchViewModel.getSearchText().observe(getViewLifecycleOwner(), s -> Log.d(Constants.LOG, s));

        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setPadding(0, 0, 0, 0);
        toolbar.setContentInsetsAbsolute(0, 0);
        TextInputEditText tiet = toolbar.findViewById(R.id.tietSearch);
        tiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_search);
            }
        });
        //Escucho el buscador
        searchViewModel.getSelected().observe(getViewLifecycleOwner(), s -> {
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
                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_cart);
                return true;
            default:
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(root));
        }
    }
}