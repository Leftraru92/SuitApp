package com.example.suitapp.fragment.addArticle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.suitapp.activity.AddArticleActivity;
import com.example.suitapp.R;
import com.example.suitapp.adapter.VariantsAdapter;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.listener.OclVariants;
import com.example.suitapp.model.DialogSelect;
import com.example.suitapp.model.Item;
import com.example.suitapp.model.Variant;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.AddArticleViewModel;

import java.util.List;

public class ArticleVariantsFragment extends Fragment implements VariantsAdapter.OnVariantListener {
    View root;
    private AddArticleViewModel mViewModel;
    RecyclerView listVariants;
    VariantsAdapter variantsAdapter;
    OclVariants oclVariants;
    AddArticleActivity activity;
    boolean fromReview;

    public static ArticleVariantsFragment newInstance() {
        return new ArticleVariantsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_article_variants, container, false);
        init();
        return root;
    }

    private void init() {
        //set
        activity = ((AddArticleActivity) getActivity());
        mViewModel = new ViewModelProvider(getActivity()).get(AddArticleViewModel.class);
        listVariants = root.findViewById(R.id.listVariants);
        variantsAdapter = new VariantsAdapter(mViewModel.getVariants().getValue(), this, R.layout.card_variant);
        listVariants.setAdapter(variantsAdapter);
        AccessDataDb accessDataDba = AccessDataDb.getInstance(getContext());
        List<Item> colors = accessDataDba.getColorsItems();
        List<Item> sizes = accessDataDba.getSizes();
        fromReview = false;
        if (getArguments() != null)
            fromReview = ArticleNameFragmentArgs.fromBundle(getArguments()).getFromReview();

        //bind
        Button btContinue = root.findViewById(R.id.btContinue);
        Button btAddVariant = root.findViewById(R.id.btAddVariant);

        //Listener
        btContinue.setOnClickListener(v -> next());
        oclVariants = new OclVariants(getContext(), mViewModel, new DialogSelect(colors), new DialogSelect(sizes));
        btAddVariant.setOnClickListener(v -> oclVariants.onClick(v));

        //viewmodel
        mViewModel.getVariants().observe(getViewLifecycleOwner(), s -> {
            if( mViewModel.getVariants().getValue().size() > 0) {
                Variant variant = mViewModel.getVariants().getValue().get(mViewModel.getVariants().getValue().size() - 1);
                mViewModel.getVariants().getValue().get(mViewModel.getVariants().getValue().size() - 1).getColor().setHex(accessDataDba.getColorHexById(variant.getColor().getId()));

                variantsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void next() {
        if (validate()) {
            if (fromReview)
                Navigation.findNavController(root).navigate(R.id.action_nav_article_variants_to_nav_article_review);
            else
                Navigation.findNavController(root).navigate(R.id.action_nav_article_variants_to_nav_article_images);
        } else
            activity.mostrarMensaje(getString(R.string.error_variant));
    }

    private boolean validate() {
        return mViewModel.getVariants().getValue().size() != 0;
    }

    @Override
    public void onVariantClick(int position, Constants.ACTION action) {
        if (action == Constants.ACTION.DELETE)
            mViewModel.deleteVariant(position);
        if (action == Constants.ACTION.UPDATE) {
            mViewModel.setEditVariant(position);
            oclVariants.onClick(root);
        }
    }
}