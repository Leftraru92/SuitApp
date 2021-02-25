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

public class ArticleNameFragment extends Fragment {
    View root;
    private AddArticleViewModel mViewModel;
    private TextInputLayout tilArticleName, tilArticleDesc;
    private TextInputEditText tietArticleName, tietArticleDesc;
    AddArticleActivity activity;
    boolean fromReview;

    public static ArticleNameFragment newInstance() {
        return new ArticleNameFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_article_name, container, false);
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
        tilArticleName = root.findViewById(R.id.tilArticleName);
        tilArticleDesc = root.findViewById(R.id.tilArticleDesc);
        tietArticleName = root.findViewById(R.id.tietArticleName);
        tietArticleDesc = root.findViewById(R.id.tietArticleDesc);

        //listener
        ((Button) root.findViewById(R.id.btContinue)).setOnClickListener(v -> next());

        //complete data
        tietArticleName.setText(mViewModel.getName().getValue());
        tietArticleDesc.setText(mViewModel.getDesc().getValue());
    }

    private void next() {
        if (validate()) {
            mViewModel.setName(tietArticleName.getText().toString());
            mViewModel.setDesc(tietArticleDesc.getText().toString());
            if (fromReview)
                Navigation.findNavController(root).navigate(R.id.action_nav_article_name_to_nav_article_review);
            else
                Navigation.findNavController(root).navigate(R.id.action_nav_article_name_to_nav_article_category);
        } else {
            activity.mostrarMensaje(getString(R.string.error_complete_form));
        }
    }

    private boolean validate() {
        int error = 0;

        error += activity.validateItem(tilArticleName, tietArticleName, true, true);
        error += activity.validateItem(tilArticleDesc, tietArticleDesc, false, true);

        return error == 0;
    }
}