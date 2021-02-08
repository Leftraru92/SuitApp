package com.example.suitapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.example.suitapp.R;
import com.example.suitapp.util.CarouselPremiumStore;
import com.example.suitapp.viewmodel.HomeViewModel;
import com.example.suitapp.viewmodel.SearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class HomeFragment extends Fragment{

    View root;
    private HomeViewModel homeViewModel;
    private SearchViewModel searchViewModel;
    ViewPager2 viewPager;
    LinearLayout llIndicator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);

        root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));

        //Defino el carousel de tiendas premium
        CarouselPremiumStore cps = new CarouselPremiumStore(root, searchViewModel);

        //searchViewModel.getSearchText().observe(getViewLifecycleOwner(), s -> Log.d(Constants.LOG, s));

        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setPadding(0, 0, 0, 0);
        toolbar.setContentInsetsAbsolute(0, 0);
        TextInputEditText tiet = toolbar.findViewById(R.id.tietSearch);
        tiet.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_search));

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