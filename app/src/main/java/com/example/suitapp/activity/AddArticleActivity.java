package com.example.suitapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.model.Article;
import com.example.suitapp.model.Store;
import com.example.suitapp.util.Constants;
import com.example.suitapp.viewmodel.AddArticleViewModel;
import com.example.suitapp.viewmodel.AddStoreViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddArticleActivity extends AppCompatActivity implements CallWebService {
    private AppBarConfiguration mAppBarConfiguration;
    InputMethodManager imm;
    Toolbar toolbar;
    final int RC_ARTICLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        int articleId = (getIntent().getExtras() != null) ?getIntent().getExtras().getInt("ARTICLEID", 0)  : 0;
        int storeId = getIntent().getExtras().getInt("STOREID", 0);

        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        toolbar = findViewById(R.id.toolbar);
        Toolbar subToolbar = findViewById(R.id.subToolbar);
        TextView tvTitle = subToolbar.findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //oculta el teclado
        //guardo el id de tienda en el view model
        AddArticleViewModel mViewModel = new ViewModelProvider(this).get(AddArticleViewModel.class);
        mViewModel.setStoreId(storeId);

        mAppBarConfiguration = new AppBarConfiguration.Builder().build();
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_article_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (articleId>0) {
            NavGraph graph = navController.getGraph();
            graph.setStartDestination(R.id.nav_article_review);
            navController.setGraph(graph);

            WebService webService = new WebService(this, RC_ARTICLE);
            webService.callService(this, Constants.WS_DOMINIO + Constants.WS_ARTICLES, "/" + articleId, Request.Method.GET, Constants.JSON_TYPE.OBJECT, null);
        }

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            appBarLayout.setExpanded(true);
            tvTitle.setText(destination.getLabel());

            if (getCurrentFocus() != null)
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            //muestro oculto toolbar
            if (destination.getId() == R.id.nav_congrats) {
                toolbar.setVisibility(View.GONE);
                subToolbar.setVisibility(View.GONE);
            } else {
                toolbar.setVisibility(View.VISIBLE);
                subToolbar.setVisibility(View.VISIBLE);
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_store_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void mostrarMensaje(String mensaje) {
        Snackbar.make(toolbar, mensaje, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.black))
                .setTextColor(getResources().getColor(R.color.white))
                .show();
    }

    public int validateItem(TextInputLayout tilItem, TextInputEditText tietItem, boolean mandatory, boolean max) {
        int error = 0;
        if (mandatory && tietItem.length() == 0) {
            tilItem.setError(getResources().getString(R.string.mandatory_field));
            error++;
        } else if (max && tietItem.length() > tilItem.getCounterMaxLength()) {
            tilItem.setError(getResources().getString(R.string.error_max_length));
            error++;
        } else {
            tilItem.setError(null);
        }
        return error;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Le paso el resultado al fragmento de arriba de la pila
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null)
            currentFragment.onActivityResult(requestCode, resultCode, data);
    }

    private Fragment getCurrentFragment() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().getPrimaryNavigationFragment();
        FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
        return fragmentManager.getPrimaryNavigationFragment();
    }

    @Override
    public void onResult(int requestCode, JSONObject response) {
        switch (requestCode) {
            case RC_ARTICLE:
                try {
                    Article mArticle = new Article(response);
                    setDataArt(mArticle);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                }
                break;
        }
    }

    private void setDataArt(Article mArticle) {
        AddArticleViewModel mViewModel = new ViewModelProvider(this).get(AddArticleViewModel.class);
        mViewModel.setStoreId(mArticle.getStoreId());
        mViewModel.setName(mArticle.getName());
        mViewModel.setDesc(mArticle.getDescription());
        mViewModel.setPrice(mArticle.getPrice());
    }

    @Override
    public void onResult(int requestCode, JSONArray response) {

    }

    @Override
    public void onError(int requestCode, String message) {

    }
}