package com.example.suitapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.suitapp.R;
import com.example.suitapp.adapter.ArticlesGroupAdapter;
import com.example.suitapp.adapter.GenresRecyclerViewAdapter;
import com.example.suitapp.adapter.StoresRecyclerViewAdapter;
import com.example.suitapp.dummy.DummyArticlesGroup;
import com.example.suitapp.dummy.DummyGenres;
import com.example.suitapp.dummy.DummyStores;
import com.example.suitapp.util.CarouselPremiumStore;
import com.example.suitapp.viewmodel.HomeViewModel;
import com.example.suitapp.viewmodel.SearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements GenresRecyclerViewAdapter.OnGenreListener, ArticlesGroupAdapter.OnGroupListener, StoresRecyclerViewAdapter.OnStoreListener {

    View root;
    private HomeViewModel homeViewModel;
    private SearchViewModel searchViewModel;
    ViewPager2 viewPager;
    LinearLayout llIndicator;
    CardView cardLogin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);

        root = inflater.inflate(R.layout.fragment_home, container, false);

        cardLogin = root.findViewById(R.id.cardLogin);

        //Defino el carousel de tiendas premium
        CarouselPremiumStore cps = new CarouselPremiumStore(root, searchViewModel);

        //Recycler generos
        RecyclerView recyclerGenres = root.findViewById(R.id.genres_list);
        recyclerGenres.setAdapter(new GenresRecyclerViewAdapter(DummyGenres.ITEMS, this));

        //Recycler grupo de articulos
        RecyclerView recyclerGroups = root.findViewById(R.id.article_groups_list);
        recyclerGroups.setAdapter(new ArticlesGroupAdapter(DummyArticlesGroup.ITEMS, this, root, R.layout.card_article_group));
        //searchViewModel.getSearchText().observe(getViewLifecycleOwner(), s -> Log.d(Constants.LOG, s));

        //Login
        Button btLogin = root.findViewById(R.id.btLogin);
        btLogin.setOnClickListener(v -> {Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_login);});

        //Store list
        RecyclerView recyclerViewStores = root.findViewById(R.id.listStores);
        recyclerViewStores.setAdapter(new StoresRecyclerViewAdapter(DummyStores.ITEMS, R.layout.card_store_circle, this));

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

    @Override
    public void onGenreClick(int position) {
        Toast.makeText(getContext(), "Toco el género  " + DummyGenres.ITEMS.get(position).getName(), Toast.LENGTH_SHORT).show();
        searchViewModel.setStore(false);
        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_article);
    }

    @Override
    public void onGroupClick(int position) {
        Toast.makeText(getContext(), "Toco el grupo  " + DummyGenres.ITEMS.get(position).getId(), Toast.LENGTH_SHORT).show();
        searchViewModel.setStore(false);
        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_article);
    }

    @Override
    public void onStoreClick(int position) {
        searchViewModel.setStoreName(DummyStores.ITEMS.get(position).getName());
        searchViewModel.setStore(true);
        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_article);
        Toast.makeText(getContext(), "Se tocó la tienda " + DummyStores.ITEMS.get(position).getName(), Toast.LENGTH_LONG).show();
    }

    private void updateUI(){
        if (getContext().getSharedPreferences("_", MODE_PRIVATE).getString("USER_EMAIL", "").equals(""))
            cardLogin.setVisibility(View.VISIBLE);
        else
            cardLogin.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}