package com.example.suitapp.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.adapter.ArticlesGroupAdapter;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.model.Article;
import com.example.suitapp.model.ArticleGroup;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.Util;
import com.example.suitapp.viewmodel.ArticleDetailViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleAddedFragment extends Fragment implements ArticlesGroupAdapter.OnGroupListener, CallWebService {
    View root;
    ArticlesGroupAdapter articlesGroupAdapter;
    ArticleDetailViewModel adViewModel;
    List<ArticleGroup> artcilegroups;
    ProgressBar pbGroups;
    private final int RC_GROUPS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_article_added, container, false);
        init();
        callWs();
        return root;
    }

    private void init() {
        //set
        adViewModel = new ViewModelProvider(getActivity()).get(ArticleDetailViewModel.class);
        artcilegroups = new ArrayList<>();
        articlesGroupAdapter = new ArticlesGroupAdapter(null, this, root, R.layout.card_article_group_horizontal_scroll);

        //bind
        pbGroups = root.findViewById(R.id.pbGroups);
        TextView tvArticleName = root.findViewById(R.id.tvArticleName);
        TextView tvVariant = root.findViewById(R.id.tvVariant);
        ImageView ivArticle = root.findViewById(R.id.ivArticle);
        RecyclerView recyclerGroups = root.findViewById(R.id.article_groups_list);
        recyclerGroups.setAdapter(articlesGroupAdapter);
        Button btSeeCart = root.findViewById(R.id.btSeeCart);
        Button btBuyCart = root.findViewById(R.id.btBuyCart);

        //listener
        btSeeCart.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_article_added_to_nav_cart));
        btBuyCart.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            //bundle.putParcelableArray("articles", articleList.toArray(new Article[articleList.size()]));
            Navigation.findNavController(root).navigate(R.id.action_nav_article_added_to_nav_select_shipping, bundle);
        });

        //viewmodel
        tvArticleName.setText(adViewModel.getName().getValue());
        String variant = "Talle: " + adViewModel.getSize().getValue() + " Color: " + adViewModel.getColor().getValue();
        tvVariant.setText(variant);
        if (adViewModel.getImagesString() != null && adViewModel.getImagesString().size() > 0) {
            byte[] decodedString = Base64.decode(adViewModel.getImagesString().get(0), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivArticle.setImageBitmap(decodedByte);
        } else
            ivArticle.setImageResource(R.drawable.ic_emoji_picture);
    }

    private void callWs() {
        WebService webService = new WebService(getContext(), RC_GROUPS);
        String params = "?groupQty=2&artQty=6&idArticle=null";
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_ARTICLES, params, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);
    }

    @Override
    public void onGroupClick(int position) {

    }

    @Override
    public void onResult(int requestCode, JSONObject response) {

    }

    @Override
    public void onResult(int requestCode, JSONArray response) {
        switch (requestCode) {
            case RC_GROUPS:
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        artcilegroups.add(new ArticleGroup(dataItem));
                    }
                    articlesGroupAdapter.setItems(artcilegroups);
                    articlesGroupAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    pbGroups.setVisibility(View.GONE);
                }
                break;
        }

    }

    @Override
    public void onError(int requestCode, String message) {

    }
}