package com.example.suitapp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.suitapp.activity.AddArticleActivity;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.model.Article;
import com.example.suitapp.model.Store;
import com.example.suitapp.util.Constants;
import com.example.suitapp.adapter.ArticleAdapter;
import com.example.suitapp.R;
import com.example.suitapp.adapter.StoresAdapter;
import com.example.suitapp.listener.OclArticlesFilter;
import com.example.suitapp.util.SingletonUser;
import com.example.suitapp.viewmodel.SearchViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends Fragment implements CallWebService, StoresAdapter.OnStoreListener, ArticleAdapter.OnArticleListener {

    View root;
    SearchViewModel searchViewModel;
    View store_banner;
    RecyclerView recyclerViewStores;
    boolean isOwner;
    final int RC_ARTICLE = 1, RC_STORE_DETAIL = 2, RC_STORE = 3;
    final int QTY_ART = 10, QTY_STORE = 6, RC_ART_DEL = 7;
    ArticleAdapter articleAdapter;
    StoresAdapter storesAdapter;
    List<Article> articleList;
    List<Store> storeList;
    ProgressBar pbArticle, pbStores;
    Button btFiltrar;
    TextView resultQty;
    SwipeRefreshLayout swipeRefresh;
    ConstraintLayout ccListStores;
    private int articlePage = 1;
    private String sharedParams;
    boolean moreArticlesAvailable = true, isLoading = false;
    int oldQty = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = this;

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                searchViewModel.setStore(0);
                NavHostFragment.findNavController(fragment).navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_articles, container, false);
        setHasOptionsMenu(true);
        init();
        return root;
    }

    private void init() {
        //set
        searchViewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
        if (getArguments() != null) {
            isOwner = ArticleFragmentArgs.fromBundle(getArguments()).getIsOwner();
            searchViewModel.setStore(ArticleFragmentArgs.fromBundle(getArguments()).getStoreId());
        }
        articleList = new ArrayList<>();
        storeList = new ArrayList<>();

        //bind
        swipeRefresh = root.findViewById(R.id.swipeRefresh);
        recyclerViewStores = root.findViewById(R.id.listStores);
        store_banner = root.findViewById(R.id.store_banner);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextInputEditText tiet = toolbar.findViewById(R.id.tietSearch);
        btFiltrar = getActivity().findViewById(R.id.btFiltrar);
        pbArticle = root.findViewById(R.id.pbArticle);
        pbStores = root.findViewById(R.id.pbStores);
        resultQty = getActivity().findViewById(R.id.resultQty);
        ccListStores = root.findViewById(R.id.ccListStores);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.cian_400), getResources().getColor(R.color.cian_200));

        //listener
        tiet.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_article_to_nav_search));
        btFiltrar.setOnClickListener(new OclArticlesFilter(getActivity()));
        swipeRefresh.setOnRefreshListener(() -> callWs());

        updateUi();

        //viewmodel
        searchViewModel.getRefresh().observe(getViewLifecycleOwner(), s -> callWs());
        //Escucho el buscador
        searchViewModel.getSearchText().observe(getViewLifecycleOwner(), s -> tiet.setText(s));
    }

    private void updateUi() {
        //modo artículo / modo tienda
        if (searchViewModel.getStore().getValue() > 0) {
            recyclerViewStores.setVisibility(View.GONE);
            store_banner.setVisibility(View.VISIBLE);
            ((TextView) root.findViewById(R.id.tvName)).setText(searchViewModel.getStoreName());
            ccListStores.setVisibility(View.GONE);
            searchViewModel.setSearchText("");
        } else {
            store_banner.setVisibility(View.GONE);
            recyclerViewStores.setVisibility(View.VISIBLE);
            storesAdapter = new StoresAdapter(null, R.layout.card_store_circle, this);
            recyclerViewStores.setAdapter(storesAdapter);
            ccListStores.setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = root.findViewById(R.id.list);

        articleAdapter = new ArticleAdapter(null, this, R.layout.card_article, isOwner);
        recyclerView.setAdapter(articleAdapter);

        NestedScrollView nestedScrollView = root.findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (nestedScrollView.canScrollVertically(1) && scrollY > oldScrollY) {
                if (((NestedScrollView) v).getChildAt(0).getBottom() <= (nestedScrollView.getHeight() + scrollY + 300)) {
                    if (moreArticlesAvailable && !isLoading)
                        callArticleWs();
                }
            }
        });

    }

    private void callWs() {
        articlePage = 1;
        moreArticlesAvailable = true;
        oldQty = 0;
        btFiltrar.setText(getResources().getString(R.string.filter) + searchViewModel.getFilterCount());
        resultQty.setText("Orden: " + searchViewModel.getOrderString());

        callArticleWs();
        //si entre en modo tienda
        if (searchViewModel.getStore().getValue() != null && searchViewModel.getStore().getValue() != 0) {

            WebService webService2 = new WebService(getContext(), RC_STORE_DETAIL);
            String params2 = "/" + searchViewModel.getStore().getValue();
            webService2.callService(this, Constants.WS_DOMINIO + Constants.WS_STORES, params2, Request.Method.GET, Constants.JSON_TYPE.OBJECT, null);
        } else { //si entre en modo artículo

            WebService webService3 = new WebService(getContext(), RC_STORE);
            int pageIdS = 1;
            String params2 = "/?storesQty=" + QTY_STORE + "&pageId=" + pageIdS;
            params2 += sharedParams;

            webService3.callService(this, Constants.WS_DOMINIO + Constants.WS_STORES, params2, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);
        }
        swipeRefresh.setRefreshing(false);
    }

    private void callArticleWs() {
        isLoading = true;
        pbArticle.setVisibility(View.VISIBLE);
        WebService webService = new WebService(getContext(), RC_ARTICLE);
        int pageId = articlePage++;

        String params = "/?artQty=" + QTY_ART + "&pageId=" + pageId;
        params += "&order=" + searchViewModel.getOrder().getValue();
        params += "&storeId=" + ((searchViewModel.getStore().getValue() != null && searchViewModel.getStore().getValue() != 0) ? searchViewModel.getStore().getValue() : "null");


        if (pageId == 1) {
            sharedParams = "";
            sharedParams += "&genderId=" + ((searchViewModel.getGenre().getValue() != null) ? searchViewModel.getGenre().getValue().getId() : "null");
            sharedParams += "&categoryId=" + ((searchViewModel.getCategory().getValue() != null) ? searchViewModel.getCategory().getValue().getId() : "null");
            sharedParams += "&minPrice=" + ((searchViewModel.getMinPrice().getValue() != null) ? searchViewModel.getMinPrice().getValue() : "null");
            sharedParams += "&maxPrice=" + ((searchViewModel.getMaxPrice().getValue() != null) ? searchViewModel.getMaxPrice().getValue() : "null");
            sharedParams += "&colourId=" + ((searchViewModel.getColor().getValue() != null) ? searchViewModel.getColor().getValue().getId() : "null");
            sharedParams += "&size=" + ((searchViewModel.getSize().getValue() != null) ? searchViewModel.getSize().getValue().getId() : "null");
            sharedParams += "&search=" + ((searchViewModel.getSearchText().getValue() != null) ? searchViewModel.getSearchText().getValue() : "");
        }
        params += sharedParams;

        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_ARTICLES, params, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Navigation.findNavController(root).navigate(R.id.action_nav_article_to_nav_cart);
                return true;
            default:
                return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(root));
        }
    }

    @Override
    public void onStoreClick(int storeId) {
        searchViewModel.clean();
        Bundle bundle = new Bundle();
        bundle.putInt("storeId", storeId);
        Navigation.findNavController(root).navigate(R.id.action_nav_article_self, bundle);
    }

    @Override
    public void onStoreEdit(int position) {

    }

    @Override
    public void onStoreDelete(int position) {

    }

    @Override
    public void onArticleClick(int articleId) {
        Bundle bundle = new Bundle();
        bundle.putInt("articleId", articleId);
        Navigation.findNavController(root).navigate(R.id.action_nav_article_to_nav_article_detail, bundle);
    }

    @Override
    public void onArticleEditClick(int articleId, View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(R.menu.menu_article_item, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_delete:
                    onDeleteArticle(articleId);
                    return true;
                case R.id.action_edit:
                    Intent intent = new Intent(getContext(), AddArticleActivity.class);
                    intent.putExtra("ARTICLEID", articleId);
                    getContext().startActivity(intent);
                    return true;
                default:
                    return true;
            }
        });
        popup.show();
    }

    private void onDeleteArticle(int articleId) {
        String param = "?email=" + SingletonUser.getInstance(getContext()).getHash() + "&articleId=" + articleId;
        WebService webService = new WebService(getContext(), RC_ART_DEL);
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_ARTICLES, param, Request.Method.DELETE, Constants.JSON_TYPE.OBJECT, null);

    }

    private void setDataStore(Store mStore) {
        ((TextView) root.findViewById(R.id.tvName)).setText(mStore.getName());
        Button btMore = root.findViewById(R.id.btMore);
        ImageView ivBanner = root.findViewById(R.id.ivBanner);

        if (mStore.getStoreCoverPhoto() != null && !mStore.getStoreCoverPhoto().equals("")) {
            byte[] decodedString = Base64.decode(mStore.getStoreCoverPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivBanner.setImageBitmap(decodedByte);
        } else {
            ivBanner.setImageResource(R.drawable.ic_emoji_picture_borderless);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("store", mStore);
        btMore.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_nav_article_to_nav_store_detail, bundle));

        //muestro boton add article
        if (isOwner) {
            Button btAddProduct = getActivity().findViewById(R.id.btAddProduct);
            btAddProduct.setText("Agregar Artículo");
            btAddProduct.setVisibility(View.VISIBLE);
            btAddProduct.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AddArticleActivity.class);
                intent.putExtra("STOREID", mStore.getId());
                getContext().startActivity(intent);
            });
        }
    }

    @Override
    public void onResult(int requestCode, JSONObject response) {
        switch (requestCode) {
            case RC_STORE_DETAIL:
                try {
                    Store mStore = new Store(response);
                    setDataStore(mStore);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    root.findViewById(R.id.pbStore).setVisibility(View.GONE);
                }
                break;
            case RC_ART_DEL:
                callWs();
                break;
        }
    }

    @Override
    public void onResult(int requestCode, JSONArray response) {
        switch (requestCode) {
            case RC_ARTICLE:
                try {
                    if (articlePage == 2)
                        articleList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        articleList.add(new Article(dataItem));
                    }
                    if (response.length() == 0 || response.length() < QTY_ART)
                        moreArticlesAvailable = false;
                    articleAdapter.setItems(articleList);
                    if (oldQty == 0)
                        articleAdapter.notifyDataSetChanged();
                    else
                        articleAdapter.notifyItemRangeInserted(oldQty, articleList.size());

                    oldQty = articleList.size();
                    if (articleList.size() > 0)
                        root.findViewById(R.id.ccNoResult).setVisibility(View.GONE);
                    else
                        root.findViewById(R.id.ccNoResult).setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    pbArticle.setVisibility(View.GONE);
                    isLoading = false;
                }
                break;
            case RC_STORE:
                try {
                    storeList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        storeList.add(new Store(dataItem));
                    }
                    storesAdapter.setItems(storeList);
                    storesAdapter.notifyDataSetChanged();
                    if (response.length() > 0)
                        root.findViewById(R.id.ccListStores).setVisibility(View.VISIBLE);
                    else
                        root.findViewById(R.id.ccListStores).setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    pbStores.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOwner)
            init();
    }
}