package com.example.suitapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.suitapp.R;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.model.Article;
import com.example.suitapp.model.Package;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.viewmodel.ShoppingViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ShopConfirmFragment extends Fragment{
    View root;
    ShoppingViewModel shoppingViewModel;
    TextView tvProduct, tvShipping, tvTotal;
    final int RC_DELETE = 1;
    int resultQty = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_shop_confirm, container, false);
        init();
        return root;
    }

    private void init() {
        shoppingViewModel = new ViewModelProvider(getActivity()).get(ShoppingViewModel.class);
        tvProduct = root.findViewById(R.id.tvProduct);
        tvShipping = root.findViewById(R.id.tvShipping);
        tvTotal = root.findViewById(R.id.tvTotal);
        Button btAddToCart = root.findViewById(R.id.btAddToCart);

        btAddToCart.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_shop_confirm_to_nav_congrats_shop));

        //viewmodel
        tvProduct.setText(shoppingViewModel.getPriceFormated());
        tvShipping.setText("$ " + (shoppingViewModel.getShippingPrice().getValue()));
        tvTotal.setText(shoppingViewModel.getTotalPriceFormated());
    }

}