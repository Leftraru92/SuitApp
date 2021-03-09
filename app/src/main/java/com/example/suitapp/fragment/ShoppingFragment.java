package com.example.suitapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.adapter.FavAdapter;
import com.example.suitapp.adapter.ShopAdapter;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.model.Article;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.viewmodel.ShoppingViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment implements CallWebService {
    View root;
    List<Article> articleList;
    ShopAdapter shopAdapter;
    ConstraintLayout ccNoResult;
    ProgressBar pbShop;
    private final int RC_SHOP = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_shopping, container, false);
        init();
        callWs();
        return root;
    }

    private void init() {
        articleList = new ArrayList<>();
        shopAdapter = new ShopAdapter(null, getContext());
        ccNoResult = root.findViewById(R.id.ccNoResult);
        RecyclerView recyclerView = root.findViewById(R.id.list);
        recyclerView.setAdapter(shopAdapter);
        pbShop = root.findViewById(R.id.pbShop);
    }

    private void callWs() {
        ccNoResult.setVisibility(View.GONE);
        pbShop.setVisibility(View.VISIBLE);
        SingletonUser sUser = SingletonUser.getInstance(getContext());
        WebService webService = new WebService(getContext(), RC_SHOP);
        String params = "?hashShopping=" +  sUser.getHash();
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_SHOPPING, params, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);
    }

    @Override
    public void onResult(int requestCode, JSONObject response) {

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
                shopAdapter.setItems(articleList);
                shopAdapter.notifyDataSetChanged();
            }else
                ccNoResult.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            pbShop.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(int requestCode, String message) {
        pbShop.setVisibility(View.GONE);
    }
}