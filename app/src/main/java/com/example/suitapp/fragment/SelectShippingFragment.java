package com.example.suitapp.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.adapter.PackagesAdapter;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.dummy.DummyPackage;
import com.example.suitapp.model.Address;
import com.example.suitapp.model.Article;
import com.example.suitapp.model.Package;
import com.example.suitapp.model.Store;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.viewmodel.ArticleDetailViewModel;
import com.example.suitapp.viewmodel.ShoppingViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SelectShippingFragment extends Fragment implements CallWebService {
    View root;
    PackagesAdapter packagesAdapter;
    ShoppingViewModel shoppingViewModel;
    List<Article> articleList;
    List<Store> storeList;
    List<Address> addressList;
    ProgressBar pbPackage, pbAddress;
    private final int STORE_ID = 1, RC_ADDRESS = 2;
    int storeCount, storeResult = 0;
    TextView tvCost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            articleList = Arrays.asList(SelectShippingFragmentArgs.fromBundle(getArguments()).getArticles());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_select_shipping, container, false);
        init();
        callWs();
        return root;
    }

    private void init() {
        //set
        setHasOptionsMenu(true);
        storeList = new ArrayList<>();
        addressList = new ArrayList<>();
        shoppingViewModel = new ViewModelProvider(getActivity()).get(ShoppingViewModel.class);
        shoppingViewModel.clean();
        packagesAdapter = new PackagesAdapter(null, shoppingViewModel, getContext());

        //bind
        pbPackage = root.findViewById(R.id.pbPackage);
        pbAddress = root.findViewById(R.id.pbAddress);
        tvCost = root.findViewById(R.id.tvCost);
        CardView cardAddress = root.findViewById(R.id.cardAddress);
        RecyclerView listPackages = root.findViewById(R.id.listPackages);
        listPackages.setAdapter(packagesAdapter);

        //listener
        cardAddress.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_select_shipping_to_nav_address));

        //viewmodel
        shoppingViewModel.getShippingPrice().observe(getViewLifecycleOwner(), s -> {
            tvCost.setText("$ " + s);
        });

    }

    private void callWs() {
        pbAddress.setVisibility(View.VISIBLE);
        SingletonUser sUser = SingletonUser.getInstance(getContext());
        WebService webServiceA = new WebService(getContext(), RC_ADDRESS);
        String params = "?hashAddress=" + sUser.getHash();
        webServiceA.callService(this, Constants.WS_DOMINIO + Constants.WS_ADDRESS, params, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

        storeResult = 0;
        storeList.clear();
        pbPackage.setVisibility(View.VISIBLE);
        List<Integer> storeIds = new ArrayList<>();
        for (Article art : articleList) {
            if (!storeIds.contains(art.getStoreId()))
                storeIds.add(art.getStoreId());
        }
        storeCount = storeIds.size();
        for (int storeId : storeIds) {
            WebService webService = new WebService(getContext(), STORE_ID);
            webService.callService(this, Constants.WS_DOMINIO + Constants.WS_STORES, "/" + storeId, Request.Method.GET, Constants.JSON_TYPE.OBJECT, null);
        }

    }

    private void next() {
        if (shoppingViewModel.isAllShippingSelected())
            Navigation.findNavController(root).navigate(R.id.action_nav_select_shipping_to_nav_card_credit);
        else
            Snackbar.make(root, "Debe seleccionar el método de envío", BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void onResult(int requestCode, JSONObject response) {
        switch (requestCode) {
            case STORE_ID:
                try {
                    Store mStore = new Store(response);
                    storeList.add(mStore);
                    storeResult++;
                    if (storeCount == storeResult)
                        generatePackage();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                }
                break;
        }
    }

    private void generatePackage() {
        List<Package> packageList = new ArrayList<>();
        for (Store store : storeList) {
            List<Article> articleStore = new ArrayList<>();
            for (Article art : articleList) {
                if (art.getStoreId() == store.getId())
                    articleStore.add(art);
            }
            packageList.add(new Package(store, articleStore));
        }
        shoppingViewModel.setPackages(packageList);
        packagesAdapter.setItems(packageList);
        packagesAdapter.notifyDataSetChanged();

        pbPackage.setVisibility(View.GONE);
        Button btContinue = root.findViewById(R.id.btContinue);
        btContinue.setVisibility(View.VISIBLE);
        btContinue.setOnClickListener(v -> next());
    }

    @Override
    public void onResult(int requestCode, JSONArray response) {
        switch (requestCode) {
            case RC_ADDRESS:
                try {
                    addressList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        addressList.add(new Address(dataItem));
                    }
                    loadAddress(addressList);

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    pbAddress.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void loadAddress(List<Address> addressList) {
        int addressId = getContext().getSharedPreferences("_", MODE_PRIVATE).getInt("ADDRESS", 0);
        TextView tvAddress = root.findViewById(R.id.tvAddress);
        for (Address address : addressList) {
            if (address.getId() == addressId) {
                tvAddress.setText(address.getAddress() + "\n" + address.getAddressDetail());
                shoppingViewModel.setProvinceId(address.getProvinceId());
                break;
            }
        }
        if (tvAddress.getText().equals(""))
            tvAddress.setText("Debe ingresar una dirección");

    }

    @Override
    public void onError(int requestCode, String message) {

    }
}