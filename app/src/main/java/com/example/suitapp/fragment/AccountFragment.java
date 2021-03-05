package com.example.suitapp.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.suitapp.activity.AddStoreActivity;
import com.example.suitapp.adapter.StoresAdapter;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.dummy.DummyStores;
import com.example.suitapp.model.Store;
import com.example.suitapp.R;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.viewmodel.SearchViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment implements StoresAdapter.OnStoreListener, CallWebService {

    private SearchViewModel searchViewModel;
    View root;
    List<Store> myStores;
    StoresAdapter storesAdapter;
    final int RC_STORES = 1;
    ProgressBar pbStores;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_account, container, false);
        init();
        callWs();
        return  root;
    }

    private void init() {
        Button btAddStore = root.findViewById(R.id.btAddStore);
        pbStores = root.findViewById(R.id.pbStores);
        btAddStore.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddStoreActivity.class);
            getContext().startActivity(intent);
        });

        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);

        RecyclerView recyclerViewStores = root.findViewById(R.id.list_stores);
        storesAdapter = new StoresAdapter(null, R.layout.card_my_store, this);
        recyclerViewStores.setAdapter(storesAdapter);
    }

    private void callWs() {
        pbStores.setVisibility(View.VISIBLE);
        String params = "?hash=" + SingletonUser.getInstance(getContext()).getHash();
        WebService webService = new WebService(getContext(), RC_STORES);
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_STORES, params, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStoreClick(int storeId) {
        searchViewModel.clean();
        Bundle bundle = new Bundle();
        bundle.putInt("storeId", storeId);
        Navigation.findNavController(root).navigate(R.id.action_nav_account_to_nav_article, bundle);
    }

    @Override
    public void onStoreEdit(int storeId) {
        Intent intent = new Intent(getContext(), AddStoreActivity.class);
        intent.putExtra("STOREID", storeId);
        getContext().startActivity(intent);
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
        pbStores.setVisibility(View.GONE);
    }
}