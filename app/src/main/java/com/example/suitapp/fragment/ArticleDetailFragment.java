package com.example.suitapp.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.suitapp.activity.MainActivity;
import com.example.suitapp.R;
import com.example.suitapp.adapter.ArticlesGroupAdapter;
import com.example.suitapp.adapter.ImageAdapter;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.database.AccessDataDb;
import com.example.suitapp.listener.OclQtySelector;
import com.example.suitapp.model.Article;
import com.example.suitapp.model.ArticleGroup;
import com.example.suitapp.model.Variant;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.util.Util;
import com.example.suitapp.viewmodel.ArticleDetailViewModel;
import com.example.suitapp.viewmodel.SearchViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailFragment extends Fragment implements ArticlesGroupAdapter.OnGroupListener, CallWebService, ImageAdapter.OnImageSelectListener {

    ArticleDetailViewModel adViewModel;
    View root;
    LayoutInflater inflater;
    MenuItem btFav;
    boolean favStatus;
    TextView tvSize, tvColor, tvStock, tvQuantity;
    int articleId;
    final int RC_ARTICLE = 1, RC_GROUPS = 2, RC_IMAGES = 3, RC_FAV = 4, RC_FAV_ACT = 5, RC_CART = 6;
    RelativeLayout.LayoutParams params, params2;
    ThemedToggleButtonGroup tbgSize, tbgColor;
    ConstraintLayout ccProgressArt;
    ArticlesGroupAdapter articlesGroupAdapter;
    List<ArticleGroup> artcilegroups;
    ProgressBar progressBarGroup;
    ViewPager2 viewPager;
    CardView cardPgrogessImage, cardNotImage, btQuantity;
    Article mArticle;
    WebService webServiceFavs;
    JSONObject bodyFav;

    public static ArticleDetailFragment newInstance() {
        return new ArticleDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        root = inflater.inflate(R.layout.fragment_article_detail, container, false);

        if (getArguments() != null)
            articleId = ArticleDetailFragmentArgs.fromBundle(getArguments()).getArticleId();
        Log.d(Constants.LOG, "ID " + articleId);

        init();

        setHasOptionsMenu(true);
        return root;
    }

    private void init() {
        //set
        int pxWidth = Util.getPxByDensity(getResources(), 40);
        int pxMargin = Util.getPxByDensity(getResources(), 3);
        params2 = new RelativeLayout.LayoutParams(pxWidth, pxWidth);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, pxWidth);
        params.setMargins(pxMargin, pxMargin, pxMargin, pxMargin);
        artcilegroups = new ArrayList<>();
        articlesGroupAdapter = new ArticlesGroupAdapter(null, this, root, R.layout.card_article_group_horizontal_scroll);
        webServiceFavs = new WebService(getContext(), RC_FAV_ACT);

        //bind
        ccProgressArt = root.findViewById(R.id.ccProgressArt);
        progressBarGroup = root.findViewById(R.id.progressBarGroup);
        viewPager = root.findViewById(R.id.viewPager);
        Button btAddToCart = root.findViewById(R.id.btAddToCart);
        Button btBuyNow = root.findViewById(R.id.btBuyNow);
        btQuantity = root.findViewById(R.id.btQuantity);
        tvColor = root.findViewById(R.id.tvColor);
        tvSize = root.findViewById(R.id.tvSize);
        tbgColor = root.findViewById(R.id.tgColors);
        tbgSize = root.findViewById(R.id.tgSize);
        cardPgrogessImage = root.findViewById(R.id.cardPgrogessImage);
        cardNotImage = root.findViewById(R.id.cardNotImage);
        tvStock = root.findViewById(R.id.tvStock);
        tvQuantity = root.findViewById(R.id.tvQuantity);

        RecyclerView recyclerGroups = root.findViewById(R.id.article_groups_list);
        recyclerGroups.setAdapter(articlesGroupAdapter);

        //Listener
        btAddToCart.setOnClickListener(v -> onAddToCart());
        btBuyNow.setOnClickListener(v -> onBuyNow());
        tbgColor.setOnSelectListener((ThemedButton btn) -> {
            if (((ThemedToggleButtonGroup) btn.getParent()).getSelectedButtons().size() > 0) {
                tvColor.setText(btn.getTag().toString());
                tvColor.setTextColor(getResources().getColor(R.color.cian_400));

                adViewModel.setColor(btn.getTag().toString());
                generateSizes(mArticle.getDistinctSizes(btn.getTag().toString()));
            } else {
                tvColor.setText(getResources().getString(R.string.select_variant));
                adViewModel.setColor(null);
                generateSizes(mArticle.getDistinctSizes());
            }
            updateStock();
            return kotlin.Unit.INSTANCE;
        });

        tbgSize.setOnSelectListener((ThemedButton btn) -> {
            if (((ThemedToggleButtonGroup) btn.getParent()).getSelectedButtons().size() > 0) {
                tvSize.setText(btn.getTag().toString());
                tvSize.setTextColor(getResources().getColor(R.color.cian_400));

                adViewModel.setSize(btn.getTag().toString());
                generateColors(mArticle.getDistinctColors(btn.getTag().toString()));
            } else {
                tvSize.setText(getResources().getString(R.string.select_variant));
                adViewModel.setSize(null);
                generateColors(mArticle.getDistinctColors());
            }
            updateStock();
            return kotlin.Unit.INSTANCE;
        });

        if (adViewModel != null && adViewModel.getArticleId().getValue() == articleId) {
            loadFromViewMdel();
        } else {
            loadFormSerer();
        }
    }

    private void loadFormSerer() {
        adViewModel = new ViewModelProvider(getActivity()).get(ArticleDetailViewModel.class);
        adViewModel.setArticleId(articleId);
        adViewModel.setQty(1);
        setOptionsFromViewModel();
        callWs();
    }

    private void setOptionsFromViewModel() {
        //Viewmodel
        adViewModel.getQty().observe(getViewLifecycleOwner(), s -> tvQuantity.setText(String.valueOf(s)));
        adViewModel.getStock().observe(getViewLifecycleOwner(), s -> {
            btQuantity.setOnClickListener(new OclQtySelector(getActivity(), s, adViewModel));
            if (adViewModel.getQty().getValue() > s)
                adViewModel.setQty(s);
            if (s > 0 && adViewModel.getQty().getValue() == 0)
                adViewModel.setQty(1);
            if (mArticle != null)
                tvStock.setText(mArticle.getStockFormatted(s));
        });
    }

    private void loadFromViewMdel() {
        ccProgressArt.setVisibility(View.GONE);
        setData(adViewModel.getArticle().getValue());
        List<String> lImagenes = adViewModel.getImagesString();
        ImageAdapter adapter = new ImageAdapter(lImagenes, this);
        viewPager.setAdapter(adapter);
        cardPgrogessImage.setVisibility(View.GONE);
        setOptionsFromViewModel();
        callRelatedArticles();
    }

    private void callWs() {
        WebService webService = new WebService(getContext(), RC_ARTICLE);
        String params = "/" + articleId;
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_ARTICLES, params, Request.Method.GET, Constants.JSON_TYPE.OBJECT, null);

        WebService webService3 = new WebService(getContext(), RC_IMAGES);
        String params3 = "/" + articleId;
        webService3.callService(this, Constants.WS_DOMINIO + Constants.WS_IMAGES, params3, Request.Method.GET, Constants.JSON_TYPE.OBJECT, null);

        callRelatedArticles();

        if (!SingletonUser.getInstance(getContext()).getHash().equals("")) {
            WebService webService4 = new WebService(getContext(), RC_FAV);
            String params4 = "?hashFavoritos=" + SingletonUser.getInstance(getContext()).getHash();
            webService4.callService(this, Constants.WS_DOMINIO + Constants.WS_FAV, params4, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

            try {
                bodyFav = new JSONObject();
                bodyFav.put("email", SingletonUser.getInstance(getContext()).getHash());
                bodyFav.put("articleId", articleId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void callRelatedArticles() {
        WebService webService2 = new WebService(getContext(), RC_GROUPS);
        String params2 = "?groupQty=2&artQty=6&idArticle=" + articleId;
        webService2.callService(this, Constants.WS_DOMINIO + Constants.WS_ARTICLES, params2, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_article_detail, menu);
        btFav = menu.getItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Navigation.findNavController(root).navigate(R.id.action_nav_article_detail_to_nav_cart);
                return true;
            case R.id.action_search:
                Navigation.findNavController(root).navigate(R.id.action_nav_article_detail_to_nav_search);
                return true;
            case R.id.action_fav:
                try {
                    onFavClick();
                } catch (JSONException ex) {
                    Log.d(Constants.LOG, ex.getMessage());
                }
                return true;
            default:
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(root));
        }
    }

    private void onFavClick() throws JSONException {
        favStatus = !favStatus;
        if (favStatus)
            webServiceFavs.callService(this, Constants.WS_DOMINIO + Constants.WS_FAV, null, Request.Method.POST, Constants.JSON_TYPE.OBJECT, bodyFav);
        else
            webServiceFavs.callService(this, Constants.WS_DOMINIO + Constants.WS_FAV, null, Request.Method.PUT, Constants.JSON_TYPE.OBJECT, bodyFav);
    }

    private void setIconFav() {
        if (favStatus)
            btFav.setIcon(R.drawable.ic_heart_solid);
        else
            btFav.setIcon(R.drawable.ic_heart_regular);
    }

    private void onAddToCart() {
        if (SingletonUser.getInstance(getContext()).isLogued()) {
            if (validateVariants()) {
                try {
                    AccessDataDb accessDataDb = AccessDataDb.getInstance(getContext());
                    WebService webService = new WebService(getContext(), RC_CART);
                    JSONObject body = new JSONObject();
                    body.put("email", SingletonUser.getInstance(getContext()).getHash());
                    body.put("articleId", articleId);
                    body.put("colorId", accessDataDb.getColorId(adViewModel.getColor().getValue()));
                    body.put("sizeId", accessDataDb.getSizeId(adViewModel.getSize().getValue()));
                    body.put("quantity", adViewModel.getQty().getValue());
                    webService.callService(this, Constants.WS_DOMINIO + Constants.WS_CART, null, Request.Method.POST, Constants.JSON_TYPE.OBJECT, body);
                    Navigation.findNavController(root).navigate(R.id.action_nav_article_detail_to_nav_article_added);
                } catch (JSONException ex) {
                    Log.d(Constants.LOG, ex.getMessage());
                }

            } else
                Snackbar.make(tvColor, getResources().getString(R.string.error_select_variant), BaseTransientBottomBar.LENGTH_LONG).show();
        } else {
            ((MainActivity) getActivity()).mostrarMensaje();
        }
    }

    private void onBuyNow() {
        if (SingletonUser.getInstance(getContext()).isLogued()) {
            if (validateVariants()) {
                AccessDataDb accessDataDb = AccessDataDb.getInstance(getContext());
                Bundle bundle = new Bundle();
                mArticle.setQuantity(adViewModel.getQty().getValue());
                mArticle.setColorId(accessDataDb.getColorId(adViewModel.getColor().getValue()));
                mArticle.setSizeId(accessDataDb.getSizeId(adViewModel.getSize().getValue()));
                Article[] articles = {mArticle};
                bundle.putParcelableArray("articles", articles);
                Navigation.findNavController(root).navigate(R.id.action_nav_article_detail_to_nav_select_shipping, bundle);
            }
            else
                Snackbar.make(tvColor, getResources().getString(R.string.error_select_variant), BaseTransientBottomBar.LENGTH_LONG).show();
        } else {
            ((MainActivity) getActivity()).mostrarMensaje();
        }
    }

    private boolean validateVariants() {
        boolean result = true;
        if (adViewModel.getColor().getValue() == null) {
            tvColor.setTextColor(getResources().getColor(R.color.error));
            result = false;
        }

        if (adViewModel.getSize().getValue() == null) {
            tvSize.setTextColor(getResources().getColor(R.color.error));
            result = false;
        }
        return result;
    }

    @Override
    public void onGroupClick(int position) {

    }

    private void setData(Article article) {
        TextView tvName = root.findViewById(R.id.tvName);
        TextView tvPrice = root.findViewById(R.id.tvPrice);
        TextView tvPriceDecimal = root.findViewById(R.id.tvPriceDecimal);
        TextView tvDescription = root.findViewById(R.id.tvDescription);
        RatingBar ratingBar = root.findViewById(R.id.ratingBar);
        TextView tvRankQty = root.findViewById(R.id.tvRankQty);

        tvName.setText(article.getName());
        tvPrice.setText(article.getPriceEnter());
        tvPriceDecimal.setText(article.getPriceDecimal());
        tvDescription.setText(article.getDescription());
        ratingBar.setRating(article.getRanking());
        tvRankQty.setText("(" + article.getCommentsQty() + ")");

        adViewModel.setStock(article.getStock());
        adViewModel.setName(article.getName());

        generateSizes(article.getDistinctSizes());
        generateColors(article.getDistinctColors());
        updateStock();

        Button btVisitStore = root.findViewById(R.id.btVisitStore);
        btVisitStore.setOnClickListener(v -> {
            SearchViewModel searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
            searchViewModel.clean();
            Bundle bundle = new Bundle();
            bundle.putInt("storeId", article.getStoreId());
            Navigation.findNavController(root).navigate(R.id.action_nav_article_detail_to_nav_article, bundle);
        });
    }

    private void generateSizes(List<String> sizeList) {
        List<ThemedButton> buttons = tbgSize.getButtons();
        if (buttons.size() == 0) {
            for (String size : sizeList) {
                ThemedButton btn = (ThemedButton) inflater.inflate(R.layout.component_size_toggle, null);
                btn.setBtnHeight(300);
                btn.setTag(size);
                btn.setText(size);
                tbgSize.addView(btn, params);
            }
        } else {
            for (ThemedButton but : buttons) {
                but.setVisibility(View.GONE);
                String siz = but.getTag().toString();
                for (String size : sizeList) {
                    if (size.equals(siz))
                        but.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void generateColors(List<Variant.Color> colors) {
        List<ThemedButton> buttons = tbgColor.getButtons();
        if (buttons.size() == 0) {
            for (Variant.Color color : colors) {
                ThemedButton btn = (ThemedButton) inflater.inflate(R.layout.component_color_toggle, null);
                btn.setTag(color.getName());
                btn.setSelectedTextColor(Color.parseColor(color.getHex()));
                btn.setTextColor(Color.parseColor(color.getHex()));
                tbgColor.addView(btn, params2);
            }
        } else {
            for (ThemedButton but : buttons) {
                but.setVisibility(View.GONE);
                String col = but.getTag().toString();
                for (Variant.Color color : colors) {
                    if (color.getName().equals(col))
                        but.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void updateStock() {
        ThemedButton tbColor = (tbgColor.getSelectedButtons().size() > 0) ? tbgColor.getSelectedButtons().get(0) : null;
        ThemedButton tbSize = (tbgSize.getSelectedButtons().size() > 0) ? tbgSize.getSelectedButtons().get(0) : null;
        String color = (tbColor != null) ? tbColor.getTag().toString() : null;
        String size = (tbSize != null) ? tbSize.getTag().toString() : null;
        int stock = mArticle.getStock(color, size);
        adViewModel.setStock(stock);
    }

    @Override
    public void onResult(int requestCode, JSONObject response) {
        switch (requestCode) {
            case RC_ARTICLE:
                try {
                    mArticle = new Article(response);
                    adViewModel.setArticle(mArticle);
                    setData(mArticle);

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    ccProgressArt.setVisibility(View.GONE);
                }
                break;
            case RC_IMAGES:
                try {
                    List<String> lImagenes = new ArrayList<>();
                    if (response.has("images") && response.getJSONArray("images") != null && response.getJSONArray("images").length() > 0) {
                        for (int i = 0; i < response.getJSONArray("images").length(); i++) {
                            JSONObject imageObject = response.getJSONArray("images").getJSONObject(i);
                            lImagenes.add(imageObject.getString("image"));
                        }
                        ImageAdapter adapter = new ImageAdapter(lImagenes, this);
                        adViewModel.setImagesString(lImagenes);
                        viewPager.setAdapter(adapter);
                    } else
                        cardNotImage.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    cardPgrogessImage.setVisibility(View.GONE);
                }
                break;
            case RC_FAV_ACT:
                setIconFav();
                if (favStatus)
                    Snackbar.make(root, "Se agregó a favoritos", BaseTransientBottomBar.LENGTH_LONG).show();
                else
                    Snackbar.make(root, "Se quitó de favoritos", BaseTransientBottomBar.LENGTH_LONG).show();

        }
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
                    progressBarGroup.setVisibility(View.GONE);
                }
                break;
            case RC_FAV:
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        Article favs = new Article(dataItem);
                        if (favs.getId() == articleId) {
                            favStatus = true;
                            setIconFav();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressBarGroup.setVisibility(View.GONE);
                }
        }
    }

    @Override
    public void onError(int requestCode, String message) {

    }

    @Override
    public void onImageClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("imageView", Constants.IMAGE_ARTICLE_DETAIL);
        bundle.putInt("imageID", position);
        Navigation.findNavController(root).navigate(R.id.action_nav_article_detail_to_nav_view_image, bundle);
    }
}