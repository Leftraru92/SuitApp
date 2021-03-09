package com.example.suitapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.adapter.AddressAdapter;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.model.Address;
import com.example.suitapp.model.Article;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AddressFragment extends Fragment implements CallWebService, AddressAdapter.OnAddressListener {
    View root;
    ProgressBar pbAddress;
    final int RC_ADDRESS = 1, RC_ADDRESS_DEL = 2;
    List<Address> addressList;
    AddressAdapter addressAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_address, container, false);
        init();
        callWs();
        return root;
    }

    private void init() {
        //set
        addressList = new ArrayList();
        int addressId = getContext().getSharedPreferences("_", MODE_PRIVATE).getInt("ADDRESS", 0);
        addressAdapter = new AddressAdapter(null, this, addressId);

        //bind
        pbAddress = root.findViewById(R.id.pbAddress);
        Button btAddStore = root.findViewById(R.id.btAddStore);
        RecyclerView listAdrress = root.findViewById(R.id.listAdrress);
        listAdrress.setAdapter(addressAdapter);

        btAddStore.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_address_to_nav_new_address));
    }

    private void callWs() {
        pbAddress.setVisibility(View.VISIBLE);
        SingletonUser sUser = SingletonUser.getInstance(getContext());
        WebService webService = new WebService(getContext(), RC_ADDRESS);
        String params = "?hashAddress=" + sUser.getHash();
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_ADDRESS, params, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

    }

    @Override
    public void onResult(int requestCode, JSONObject response) {
        switch (requestCode) {
            case RC_ADDRESS_DEL:
                callWs();
                break;
        }
    }

    @Override
    public void onResult(int requestCode, JSONArray response) {
        try {
            addressList.clear();
            for (int i = 0; i < response.length(); i++) {
                JSONObject dataItem = response.getJSONObject(i);
                addressList.add(new Address(dataItem));
            }
            addressAdapter.setItems(addressList);
            addressAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            pbAddress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(int requestCode, String message) {
        pbAddress.setVisibility(View.GONE);
    }

    @Override
    public void onDeleteClick(int addressId) {
        AlertDialog dialogo = new AlertDialog
                .Builder(getContext()) // NombreDeTuActividad.this, o getActivity() si es dentro de un fragmento
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDeleteAddress(addressId);
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setTitle("Confirmar") // El título
                .setMessage("¿Deseas eliminar la dirección?") // El mensaje
                .create();
        dialogo.show();
    }

    private void onDeleteAddress(int addressId) {
        String param = "?email=" + SingletonUser.getInstance(getContext()).getHash() + "&addressId=" + addressId;
        WebService webService = new WebService(getContext(), RC_ADDRESS_DEL);
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_ADDRESS, param, Request.Method.DELETE, Constants.JSON_TYPE.OBJECT, null);
    }

    @Override
    public void onEditClick(int addressId) {
        getContext().getSharedPreferences("_", MODE_PRIVATE).edit().putInt("ADDRESS", addressId).apply();
        Navigation.findNavController(root).navigateUp();
    }

    @Override
    public void onResume() {
        super.onResume();
        callWs();
    }
}