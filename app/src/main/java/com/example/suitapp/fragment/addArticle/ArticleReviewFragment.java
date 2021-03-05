package com.example.suitapp.fragment.addArticle;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.adapter.AddArticleImagesAdapter;
import com.example.suitapp.adapter.VariantsAdapter;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.AddArticleViewModel;

import org.json.JSONObject;

public class ArticleReviewFragment extends Fragment implements VariantsAdapter.OnVariantListener, AddArticleImagesAdapter.OnImageListener {
    View root;
    AddArticleViewModel mViewModel;
    RecyclerView recyclerListVariants, recyclerListImages;
    VariantsAdapter variantsAdapter;
    AddArticleImagesAdapter imagesAdapter;


    public static ArticleReviewFragment newInstance() {
        return new ArticleReviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_article_review, container, false);
        init();
        return root;
    }

    private void init() {
        //set
        mViewModel = new ViewModelProvider(getActivity()).get(AddArticleViewModel.class);
        variantsAdapter = new VariantsAdapter(mViewModel.getVariants().getValue(), this, R.layout.card_variant_mini);
        imagesAdapter = new AddArticleImagesAdapter(mViewModel.getImages().getValue(), this, false);
        recyclerListVariants = root.findViewById(R.id.listVariants);
        recyclerListVariants.setAdapter(variantsAdapter);
        recyclerListImages = root.findViewById(R.id.listImages);
        recyclerListImages.setAdapter(imagesAdapter);

        //bind
        CardView cardName = root.findViewById(R.id.cardName);
        CardView cardCategory = root.findViewById(R.id.cardCategory);
        CardView cardGenre = root.findViewById(R.id.cardGenre);
        CardView cardVariants = root.findViewById(R.id.cardVariants);
        CardView cardImages = root.findViewById(R.id.cardImages);
        CardView cardPrice = root.findViewById(R.id.cardPrice);

        TextView tvName = root.findViewById(R.id.tvName);
        TextView tvDescription = root.findViewById(R.id.tvDescription);
        TextView tvCategory = root.findViewById(R.id.tvCategory);
        TextView tvGenre = root.findViewById(R.id.tvGenre);
        TextView tvPrice = root.findViewById(R.id.tvPrice);
        Button btContinue = root.findViewById(R.id.btContinue);

        //listener
        cardName.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_article_review_to_nav_article_name));
        cardCategory.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_article_review_to_nav_article_category));
        cardGenre.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_article_review_to_nav_article_genre));
        cardVariants.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_article_review_to_nav_article_variants));
        cardImages.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_article_review_to_nav_article_images));
        cardPrice.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_article_review_to_nav_article_price));
        btContinue.setOnClickListener(v -> next());


        //viewmodel
        mViewModel.getName().observe(getViewLifecycleOwner(), s -> tvName.setText(s));
        mViewModel.getDesc().observe(getViewLifecycleOwner(), s -> {
            if (s.equals(""))
                tvDescription.setVisibility(View.GONE);
            else
                tvDescription.setText(s);
        });
        mViewModel.getCategory().observe(getViewLifecycleOwner(), s -> tvCategory.setText(s.getName()));
        mViewModel.getGenre().observe(getViewLifecycleOwner(), s -> tvGenre.setText(s.getName()));
        mViewModel.getPrice().observe(getViewLifecycleOwner(), s -> tvPrice.setText(String.valueOf(s)));
    }

    private void next() {
        JSONObject jsonStore = mViewModel.toJSON();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isArticle", true);
        Navigation.findNavController(root).navigate(R.id.action_nav_article_review_to_nav_congrats, bundle);
    }

    @Override
    public void onVariantClick(int adapterPosition, Constants.ACTION action) {
        Navigation.findNavController(root).navigate(R.id.action_nav_article_review_to_nav_article_variants);
    }

    @Override
    public void onImageClick(int position) {
        Navigation.findNavController(root).navigate(R.id.action_nav_article_review_to_nav_article_images);
    }

    @Override
    public void onImageDelete(int position) {

    }
}