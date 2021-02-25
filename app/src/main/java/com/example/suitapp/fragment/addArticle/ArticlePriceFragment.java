package com.example.suitapp.fragment.addArticle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.suitapp.activity.AddArticleActivity;
import com.example.suitapp.R;
import com.example.suitapp.viewmodel.AddArticleViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ArticlePriceFragment extends Fragment {
    View root;
    private AddArticleViewModel mViewModel;
    private TextInputLayout tilPrice;
    private TextInputEditText tietPrice;
    AddArticleActivity activity;
    boolean fromReview;

    public static ArticlePriceFragment newInstance() {
        return new ArticlePriceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_article_price, container, false);
        init();
        return root;
    }

    private void init() {

        //set
        activity = ((AddArticleActivity) getActivity());
        mViewModel = new ViewModelProvider(getActivity()).get(AddArticleViewModel.class);
        fromReview = false;
        if (getArguments() != null)
            fromReview = ArticleNameFragmentArgs.fromBundle(getArguments()).getFromReview();

        //bind
        tilPrice = root.findViewById(R.id.tilPrice);
        tietPrice = root.findViewById(R.id.tietPrice);

        //listener
        ((Button) root.findViewById(R.id.btContinue)).setOnClickListener(v -> next());

        //complete data
        if (mViewModel.getPrice().getValue() != null)
            tietPrice.setText(mViewModel.getPrice().getValue().toString());
    }

    private void next() {
        if (validate()) {
            mViewModel.setPrice(Float.parseFloat(tietPrice.getText().toString()));
            Navigation.findNavController(root).navigate(R.id.action_nav_article_price_to_nav_article_review);
        } else {
            activity.mostrarMensaje(getString(R.string.error_complete_form));
        }
    }

    private boolean validate() {
        int error = 0;

        error += activity.validateItem(tilPrice, tietPrice, true, true);

        return error == 0;
    }
}