package com.example.suitapp.fragment;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
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
    final int RC_STORES = 1, RC_STORE_DEL = 2;
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
        Button btAddress = root.findViewById(R.id.btAddress);
        pbStores = root.findViewById(R.id.pbStores);

        //listener
        btAddStore.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddStoreActivity.class);
            getContext().startActivity(intent);
        });
        btAddress.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_account_to_nav_address));

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
        AlertDialog dialogo = new AlertDialog
                .Builder(getContext()) // NombreDeTuActividad.this, o getActivity() si es dentro de un fragmento
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteStore(position);
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setTitle("Confirmar") // El título
                .setMessage("¿Deseas eliminar la tienda?\n" +
                        "Se eliminarán los artículos asociados") // El mensaje
                .create();
        dialogo.show();
    }

    private void deleteStore(int position) {
        String param = "?email="+ SingletonUser.getInstance(getContext()).getHash() + "&storeId=" + position;
        WebService webService = new WebService(getContext(), RC_STORE_DEL);
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_STORES, param, Request.Method.DELETE, Constants.JSON_TYPE.OBJECT, null);
    }

    @Override
    public void onResult(int requestCode, JSONObject response) {
        switch (requestCode) {
            case RC_STORE_DEL:
                callWs();
                break;
        }
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

    @Override
    public void onResume() {
        super.onResume();
        callWs();
    }
}