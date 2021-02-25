package com.example.suitapp.fragment.addStore;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.suitapp.activity.AddArticleActivity;
import com.example.suitapp.R;
import com.example.suitapp.fragment.addArticle.ArticleNameFragmentArgs;

public class CongratsFragment extends Fragment {
    View root;
    ProgressBar progress_circular;
    ConstraintLayout clPublicado;
    boolean isArticle;

    public static CongratsFragment newInstance() {
        return new CongratsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_congrats, container, false);
        init();
        return root;
    }

    private void init() {
        //set
        isArticle = false;
        if (getArguments() != null)
            isArticle = ArticleNameFragmentArgs.fromBundle(getArguments()).getFromReview();

        //bind
        Button btAddProduct = root.findViewById(R.id.btAddProduct);
        Button btContinue = root.findViewById(R.id.btContinue);
        TextView lbSub = root.findViewById(R.id.lbSub);
        TextView lbSub2 = root.findViewById(R.id.lbSub2);
        progress_circular = root.findViewById(R.id.progress_circular);
        clPublicado = root.findViewById(R.id.clPublicado);

        //set
        if(isArticle){
            lbSub.setText(getString(R.string.congrats_art));
            lbSub2.setText(getString(R.string.congrats_det_article));
        }else{
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

        Runnable runnable = () -> setResult(true);
        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);
    }

    private void setResult(boolean result){
        progress_circular.setVisibility((result) ? View.GONE : View.VISIBLE);
        clPublicado.setVisibility((result) ? View.VISIBLE : View.GONE);
    }
}