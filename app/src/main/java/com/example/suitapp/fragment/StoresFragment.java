package com.example.suitapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.adapter.StoresAdapter;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.dummy.DummyStores;
import com.example.suitapp.model.Store;
import com.example.suitapp.util.CarouselPremiumStore;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.SearchViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoresFragment extends Fragment implements StoresAdapter.OnStoreListener, CallWebService {
    View root;
    StoresAdapter storesAdapter;
    ProgressBar pbStores;
    final int RC_STORES = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_store_list, container, false);
        init();
        callWs();
        return root;
    }

    private void init() {
        RecyclerView storeList = root.findViewById(R.id.storeList);
        pbStores = root.findViewById(R.id.pbStores);

        storesAdapter = new StoresAdapter(null, R.layout.card_stores, this);
        storeList.setAdapter(storesAdapter);
    }

    private void callWs() {
        WebService webService = new WebService(getContext(), RC_STORES);
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_STORES, null, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);
    }

    @Override
    public void onStoreClick(int storeId) {
        SearchViewModel searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        searchViewModel.clean();

        Bundle bundle = new Bundle();
        bundle.putInt("storeId", storeId);
        Navigation.findNavController(root).navigate(R.id.action_nav_stores_to_nav_article, bundle);
    }

    @Override
    public void onStoreEdit(int position) {

    }

    @Override
    public void onStoreDelete(int position) {

    }

    @Override
    public void onResult(int requestCode, JSONObject response) {

    }

    @Override
    public void onResult(int requestCode, JSONArray response) {
        switch (requestCode){
            case RC_STORES:
                try {
                    List<Store> storeList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        storeList.add(new Store(dataItem));
                    }
                    storesAdapter.setItems(storeList);
                    storesAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    pbStores.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {

    }
}