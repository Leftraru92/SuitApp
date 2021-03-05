package com.example.suitapp.fragment.addStore;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.suitapp.activity.AddArticleActivity;
import com.example.suitapp.R;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.fragment.ArticleDetailFragmentArgs;
import com.example.suitapp.fragment.ArticleFragmentArgs;
import com.example.suitapp.fragment.addArticle.ArticleNameFragmentArgs;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.viewmodel.AddArticleViewModel;
import com.example.suitapp.viewmodel.AddStoreViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class CongratsFragment extends Fragment implements CallWebService {
    View root;
    ProgressBar progress_circular;
    ConstraintLayout clPublicado, clError;
    boolean isArticle;
    AddStoreViewModel mViewModelStore;
    AddArticleViewModel mViewModelArt;
    final int RC_ARTICLE = 1, RC_STORE = 2;

    public static CongratsFragment newInstance() {
        return new CongratsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_congrats, container, false);
        init();
        callWs();
        return root;
    }

    private void init() {
        //set
        isArticle = false;
        //if (getArguments() != null)
        //  isArticle = ArticleNameFragmentArgs.fromBundle(getArguments()).getFromReview();
        if (getArguments() != null)
            isArticle = CongratsFragmentArgs.fromBundle(getArguments()).getIsArticle();

        //bind
        Button btAddProduct = root.findViewById(R.id.btAddProduct);
        Button btContinue = root.findViewById(R.id.btContinue);
        Button btTryAgain = root.findViewById(R.id.btTryAgain);
        Button btBack = root.findViewById(R.id.btBack);
        TextView lbSub = root.findViewById(R.id.lbSub);
        TextView lbSub2 = root.findViewById(R.id.lbSub2);
        progress_circular = root.findViewById(R.id.progress_circular);
        clPublicado = root.findViewById(R.id.clPublicado);
        clError = root.findViewById(R.id.clError);

        //set
        if (isArticle) {
            lbSub.setText(getString(R.string.congrats_art));
            lbSub2.setText(getString(R.string.congrats_det_article));
        } else {
            lbSub.setText(getString(R.string.congrats_store));
            lbSub2.setText(getString(R.string.congrats_det_store));
        }

        //listener
        btAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddArticleActivity.class);
            getContext().startActivity(intent);
            getActivity().finish();
        });
        btContinue.setOnClickListener(v -> getActivity().finish());
        btBack.setOnClickListener(v -> getActivity().finish());
        btTryAgain.setOnClickListener(v -> callWs());
    }

    private void callWs() {
        progress_circular.setVisibility(View.VISIBLE);
        clError.setVisibility(View.GONE);
        clPublicado.setVisibility(View.GONE);

        if (isArticle)
            sendPostArticle();
        else
            sendPostStore();
    }

    private void sendPostArticle() {
    }

    private void sendPostStore() {
        mViewModelStore = new ViewModelProvider(getActivity()).get(AddStoreViewModel.class);
        SingletonUser sUsuario = SingletonUser.getInstance(getContext());
        JSONObject jsonStore = mViewModelStore.toJSON(sUsuario.getHash());

        WebService ws = new WebService(getContext(), RC_STORE);
        ws.callService(this, Constants.WS_DOMINIO + Constants.WS_STORES, null, Request.Method.POST, Constants.JSON_TYPE.OBJECT, jsonStore);
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
        switch (requestCode) {
            case RC_STORE:
                setResult(true);
                break;
        }

    }

    @Override
    public void onResult(int requestCode, JSONArray response) {

    }

    @Override
    public void onError(int requestCode, String message) {
        switch (requestCode) {
            case RC_STORE:
                setResult(false);
                break;
        }
    }
}