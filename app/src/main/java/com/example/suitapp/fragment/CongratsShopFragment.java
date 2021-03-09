package com.example.suitapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.activity.AddArticleActivity;
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

public class CongratsShopFragment extends Fragment implements CallWebService {
    View root;
    ProgressBar progress_circular;
    ConstraintLayout clPublicado, clError;
    ShoppingViewModel shoppingViewModel;
    int resultQty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_congrats_shop, container, false);
        init();
        callWs();
        return root;
    }

    private void init() {
        //bind
        shoppingViewModel = new ViewModelProvider(getActivity()).get(ShoppingViewModel.class);
        Button btAddProduct = root.findViewById(R.id.btAddProduct);
        Button btContinue = root.findViewById(R.id.btContinue);
        Button btTryAgain = root.findViewById(R.id.btTryAgain);
        Button btBack = root.findViewById(R.id.btBack);
        TextView lbSub = root.findViewById(R.id.lbSub);
        TextView lbSub2 = root.findViewById(R.id.lbSub2);
        progress_circular = root.findViewById(R.id.progress_circular);
        clPublicado = root.findViewById(R.id.clPublicado);
        clError = root.findViewById(R.id.clError);

        //listener
        btAddProduct.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_congrats_shop_to_nav_shopping));
        btContinue.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_congrats_shop_to_nav_home));
        btBack.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_congrats_shop_to_nav_home));
        btTryAgain.setOnClickListener(v -> callWs());
    }

    private void callWs() {
        resultQty = 0;
        int addressId = getContext().getSharedPreferences("_", MODE_PRIVATE).getInt("ADDRESS", 0);

        String email = SingletonUser.getInstance(getContext()).getHash();
        for (Package pack: shoppingViewModel.getPackages().getValue() ) {
            WebService webService = new WebService(getContext(), pack.getStore().getId());
            JSONObject body = pack.toJson(email, addressId);

            webService.callService(this, Constants.WS_DOMINIO + Constants.WS_SHOPPING, null, Request.Method.POST, Constants.JSON_TYPE.OBJECT, body);
        }
    }

    private void setResult(boolean result) {
        progress_circular.setVisibility(View.GONE);
        if (result) {
            clPublicado.setVisibility(View.VISIBLE);
            clError.setVisibility(View.GONE);
        }else{
            clError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResult(int requestCode, JSONObject response) {
        resultQty++;
        if(resultQty == shoppingViewModel.getPackages().getValue().size()){
            setResult(true);
        }
    }

    @Override
    public void onResult(int requestCode, JSONArray response) {

    }

    @Override
    public void onError(int requestCode, String message) {
        setResult(false);
    }

}