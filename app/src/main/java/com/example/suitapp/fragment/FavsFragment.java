package com.example.suitapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.example.suitapp.adapter.FavAdapter;
import com.example.suitapp.R;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.model.Article;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavsFragment extends Fragment implements FavAdapter.OnFavListener, CallWebService {

    View root;
    private List<Article> articleList;
    FavAdapter favAdapter;
    ProgressBar pbFavs;
    ConstraintLayout ccNoResult;
    final int RC_FAV = 1, RC_FAV_ACT=2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_favs_list, container, false);
        setHasOptionsMenu(true);
        init();
        callWs();
        return root;
    }

    private void init() {
        articleList = new ArrayList<>();
        favAdapter = new FavAdapter(null, this);
        ccNoResult = root.findViewById(R.id.ccNoResult);
        RecyclerView recyclerView = root.findViewById(R.id.list);
        recyclerView.setAdapter(favAdapter);
        pbFavs = root.findViewById(R.id.pbFavs);
    }

    private void callWs() {
        ccNoResult.setVisibility(View.GONE);
        pbFavs.setVisibility(View.VISIBLE);
        SingletonUser sUser = SingletonUser.getInstance(getContext());
        WebService webService = new WebService(getContext(), RC_FAV);
        String params = "?hashFavoritos=" +  sUser.getHash();
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_FAV, params, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favs, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Navigation.findNavController(root).navigate(R.id.action_nav_favs_to_nav_cart);
                return true;
            case R.id.action_search:
                Navigation.findNavController(root).navigate(R.id.action_nav_favs_to_nav_search);
                return true;
            default:
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(root));
        }
    }

    @Override
    public void onFavClick(int articleId) {
        Bundle bundle = new Bundle();
        bundle.putInt("articleId", articleId);

        Navigation.findNavController(root).navigate(R.id.action_nav_favs_to_nav_article_detail, bundle);
    }

    @Override
    public void onDeleteClick(int articleId) {
        JSONObject bodyFav = new JSONObject();
        try {
            bodyFav.put("email", SingletonUser.getInstance(getContext()).getHash());
            bodyFav.put("articleId", articleId);
        }catch (JSONException ex){
            ex.getMessage();
        }
        WebService webServiceFavs = new WebService(getContext(), RC_FAV_ACT);
        webServiceFavs.callService(this, Constants.WS_DOMINIO + Constants.WS_FAV, null, Request.Method.PUT, Constants.JSON_TYPE.OBJECT, bodyFav);
    }

    @Override
    public void onResult(int requestCode, JSONObject response) {
        callWs();
    }

    @Override
    public void onResult(int requestCode, JSONArray response) {
        try {
            articleList.clear();
            for (int i = 0; i < response.length(); i++) {
                JSONObject dataItem = response.getJSONObject(i);
                articleList.add(new Article(dataItem));
            }
            if(articleList.size() > 0) {
                favAdapter.setItems(articleList);
                favAdapter.notifyDataSetChanged();
            }else
                ccNoResult.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            pbFavs.setVisibility(View.GONE);
        }

    }

    @Override
    public void onError(int requestCode, String message) {
        pbFavs.setVisibility(View.GONE);
    }
}