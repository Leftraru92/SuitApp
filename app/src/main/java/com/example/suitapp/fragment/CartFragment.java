package com.example.suitapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.example.suitapp.adapter.CartAdapter;
import com.example.suitapp.R;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.model.Article;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.viewmodel.SearchViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment implements CallWebService, CartAdapter.OnCartListener {

    View root;
    private List<Article> articleList;
    CartAdapter cartAdapter;
    ProgressBar pbCart;
    ConstraintLayout ccNoResult;
    final int RC_CART = 1, RC_DELETE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cart_list, container, false);
        init();
        callWs();
        return root;
    }

    private void init() {
        setHasOptionsMenu(true);

        cartAdapter = new CartAdapter(null, getContext(), this);
        RecyclerView cartList = root.findViewById(R.id.cartList);
        ccNoResult = root.findViewById(R.id.ccNoResult);
        pbCart = root.findViewById(R.id.pbCart);
        cartList.setAdapter(cartAdapter);
        articleList = new ArrayList<>();
    }

    private void showBuyCart() {
        if (getActivity() != null) {
            Button btAddProduct = getActivity().findViewById(R.id.btAddProduct);
            btAddProduct.setVisibility(View.VISIBLE);
            btAddProduct.setText("Continuar compra");
            btAddProduct.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelableArray("articles", articleList.toArray(new Article[articleList.size()]));
                Navigation.findNavController(root).navigate(R.id.action_nav_cart_to_nav_select_shipping, bundle);
            });
        }
    }

    private void callWs() {
        ccNoResult.setVisibility(View.GONE);
        pbCart.setVisibility(View.VISIBLE);
        SingletonUser sUser = SingletonUser.getInstance(getContext());
        WebService webService = new WebService(getContext(), RC_CART);
        String params = "?hashCarrito=" + sUser.getHash();
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_CART, params, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cart, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Navigation.findNavController(root).navigate(R.id.action_nav_cart_to_nav_search);
                return true;
            default:
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(root));
        }
    }

    @Override
    public void onResult(int requestCode, JSONObject response) {
        switch (requestCode) {
            case RC_DELETE:
                callWs();
                break;
        }
    }

    @Override
    public void onResult(int requestCode, JSONArray response) {
        switch (requestCode) {
            case RC_CART:
                try {
                    articleList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        articleList.add(new Article(dataItem));
                    }
                    if (articleList.size() > 0) {
                        cartAdapter.setItems(articleList);
                        cartAdapter.notifyDataSetChanged();
                        showBuyCart();
                    } else
                        ccNoResult.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    pbCart.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {
        pbCart.setVisibility(View.GONE);
    }

    @Override
    public void onDeleteClick(Article article) {
        JSONObject body = new JSONObject();
        try {
            body.put("email", SingletonUser.getInstance(getContext()).getHash());
            body.put("articleId", article.getId());
            body.put("colorId", article.getColorId());
            body.put("sizeId", article.getSizeId());
            body.put("quantity", article.getQuantity());
        } catch (JSONException ex) {
            Log.d(Constants.LOG, ex.getMessage());
        }

        WebService webService = new WebService(getContext(), RC_DELETE);
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_CART, null, Request.Method.PUT, Constants.JSON_TYPE.OBJECT, body);
    }

    @Override
    public void onMoreClick(int storeId) {
        SearchViewModel searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        searchViewModel.clean();
        Bundle bundle = new Bundle();
        bundle.putInt("storeId", storeId);
        Navigation.findNavController(root).navigate(R.id.action_nav_cart_to_nav_article, bundle);
    }
}