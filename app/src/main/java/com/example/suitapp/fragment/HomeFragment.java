package com.example.suitapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.adapter.ArticlesGroupAdapter;
import com.example.suitapp.adapter.GenderAdapter;
import com.example.suitapp.adapter.StoreGroupAdapter;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.model.ArticleGroup;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Store;
import com.example.suitapp.model.StoreGroup;
import com.example.suitapp.model.Variant;
import com.example.suitapp.util.CarouselPremiumStore;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.SearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements CallWebService, GenderAdapter.OnGenreListener, ArticlesGroupAdapter.OnGroupListener{

    View root;
    private SearchViewModel searchViewModel;
    CardView cardLogin;
    List<Gender> genderList;
    List<ArticleGroup> artcilegroups;
    List<StoreGroup> storeGroups;
    GenderAdapter genderAdapter;
    ArticlesGroupAdapter articlesGroupAdapter;
    StoreGroupAdapter storeGroupAdapter;
    private final int RC_GROUPS = 1, RC_STORES = 2, RC_STORES_PREMIUM = 3;
    ProgressBar pbGroups, pbGroupStores, pbPremiumStores;
    SwipeRefreshLayout swipeRefresh;
    boolean isBannerLoad = false;

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
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        setHasOptionsMenu(true);
        artcilegroups = new ArrayList<>();
        storeGroups = new ArrayList<>();

        //Bind
        swipeRefresh = root.findViewById(R.id.swipeRefresh);
        cardLogin = root.findViewById(R.id.cardLogin);
        RecyclerView recyclerGenres = root.findViewById(R.id.genres_list);
        RecyclerView recyclerGroups = root.findViewById(R.id.article_groups_list);
        RecyclerView recyclerViewStores = root.findViewById(R.id.listStores);
        Button btLogin = root.findViewById(R.id.btLogin);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextInputEditText tiet = toolbar.findViewById(R.id.tietSearch);
        pbGroups = root.findViewById(R.id.pbGroups);
        pbGroupStores = root.findViewById(R.id.pbGroupStores);
        pbPremiumStores = root.findViewById(R.id.pbPremiumStores);

        //Listener
        btLogin.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_login);
        });
        tiet.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_search));
        tiet.setText(getResources().getString(R.string.search_hint));
        swipeRefresh.setOnRefreshListener(() -> callWs());

        //Set post bind
        genderAdapter = new GenderAdapter(null, this, getContext());
        articlesGroupAdapter = new ArticlesGroupAdapter(null, this, root, R.layout.card_article_group);
        storeGroupAdapter = new StoreGroupAdapter(null, root);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.cian_400), getResources().getColor(R.color.cian_200));

        recyclerGenres.setAdapter(genderAdapter);
        recyclerGroups.setAdapter(articlesGroupAdapter);
        recyclerViewStores.setAdapter(storeGroupAdapter);

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

        WebService webService2 = new WebService(getContext(), RC_STORES);
        String params2 = "?groupQty=1&storeQty=4";
        webService2.callService(this, Constants.WS_DOMINIO + Constants.WS_STORES, params2, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

        WebService webService3 = new WebService(getContext(), RC_STORES_PREMIUM);
        String params3 = "?storeQty=3";
        webService3.callService(this, Constants.WS_DOMINIO + Constants.WS_STORES, params3, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

        swipeRefresh.setRefreshing(false);
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
        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_article);
    }

    @Override
    public void onGroupClick(int position) {
        //Toast.makeText(getContext(), "Toco el grupo  " + artcilegroups.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_article);
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
                    artcilegroups.clear();
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
            case RC_STORES:
                try {
                    storeGroups.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        storeGroups.add(new StoreGroup(dataItem));
                    }
                    storeGroupAdapter.setItems(storeGroups);
                    storeGroupAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    pbGroupStores.setVisibility(View.GONE);
                }
                break;
            case RC_STORES_PREMIUM:
                try {
                    List<Store> storeList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        storeList.add(new Store(dataItem));
                    }
                    //Defino el carousel de tiendas premium
                    new CarouselPremiumStore(root, searchViewModel, storeList, !isBannerLoad);
                    isBannerLoad = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    pbPremiumStores.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {

    }
}