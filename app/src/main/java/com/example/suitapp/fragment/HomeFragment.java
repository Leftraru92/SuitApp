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
import android.widget.ProgressBar;
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

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.adapter.ArticlesGroupAdapter;
import com.example.suitapp.adapter.GenderAdapter;
import com.example.suitapp.adapter.StoresRecyclerViewAdapter;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.database.QueryDbInsert;
import com.example.suitapp.dummy.DummyArticlesGroup;
import com.example.suitapp.dummy.DummyStores;
import com.example.suitapp.model.ArticleGroup;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.util.CarouselPremiumStore;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.HomeViewModel;
import com.example.suitapp.viewmodel.SearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements CallWebService, GenderAdapter.OnGenreListener, ArticlesGroupAdapter.OnGroupListener, StoresRecyclerViewAdapter.OnStoreListener {

    View root;
    private HomeViewModel homeViewModel;
    private SearchViewModel searchViewModel;
    ViewPager2 viewPager;
    LinearLayout llIndicator;
    CardView cardLogin;
    List<Gender> genderList;
    GenderAdapter genderAdapter;
    ArticlesGroupAdapter articlesGroupAdapter;
    private final int RC_GROUPS = 1;
    List<ArticleGroup> artcilegroups;
    ProgressBar pbGroups;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        getData();
        callWs();
        return root;
    }

    private void init() {
        //Set
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        //Defino el carousel de tiendas premium
        CarouselPremiumStore cps = new CarouselPremiumStore(root, searchViewModel);
        setHasOptionsMenu(true);
        artcilegroups = new ArrayList<>();

        //Bind
        cardLogin = root.findViewById(R.id.cardLogin);
        RecyclerView recyclerGenres = root.findViewById(R.id.genres_list);
        RecyclerView recyclerGroups = root.findViewById(R.id.article_groups_list);
        RecyclerView recyclerViewStores = root.findViewById(R.id.listStores);
        Button btLogin = root.findViewById(R.id.btLogin);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextInputEditText tiet = toolbar.findViewById(R.id.tietSearch);
        pbGroups = root.findViewById(R.id.pbGroups);

        //Listener
        btLogin.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_login);
        });
        tiet.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_search));

        //Set post bind
        genderAdapter = new GenderAdapter(null, this, getContext());
        recyclerGenres.setAdapter(genderAdapter);
        articlesGroupAdapter = new ArticlesGroupAdapter(null, this, root, R.layout.card_article_group);
        recyclerGroups.setAdapter(articlesGroupAdapter);
        recyclerViewStores.setAdapter(new StoresRecyclerViewAdapter(DummyStores.ITEMS, R.layout.card_store_circle, this));
        toolbar.setPadding(0, 0, 0, 0);
        toolbar.setContentInsetsAbsolute(0, 0);
    }

    private void getData() {
        AccessDataDb accessDataDba = AccessDataDb.getInstance(getContext());

        genderList = accessDataDba.getGenders();
        genderAdapter.setItems(genderList);
        genderAdapter.notifyDataSetChanged();
    }

    private void callWs() {
        pbGroups.setVisibility(View.VISIBLE);
        WebService webService = new WebService(getContext(), RC_GROUPS);
        String params = "?groupQty=2&artQty=4&idArticle=null";
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_ARTICLES, params, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);
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
        searchViewModel.setGenre(genderList.get(position));
        searchViewModel.setStore(false);
        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_article);
    }

    @Override
    public void onGroupClick(int position) {
        Toast.makeText(getContext(), "Toco el grupo  " + DummyArticlesGroup.ITEMS.get(position).getTitle(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStoreEdit(int position) {

    }

    @Override
    public void onStoreDelete(int position) {

    }

    private void updateUI() {
        searchViewModel.clean();
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

    @Override
    public void onResult(int requestCode, JSONObject response) {

    }

    @Override
    public void onResult(int requestCode, JSONArray response) {
        switch (requestCode) {
            case RC_GROUPS:
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        artcilegroups.add(new ArticleGroup(dataItem));
                    }
                    articlesGroupAdapter.setItems(artcilegroups);
                    articlesGroupAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    pbGroups.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {

    }
}