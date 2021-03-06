package com.example.suitapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.model.Store;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.Util;
import com.example.suitapp.viewmodel.AddStoreViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddStoreActivity extends AppCompatActivity implements CallWebService {
    private AppBarConfiguration mAppBarConfiguration;
    InputMethodManager imm;
    Toolbar toolbar;
    final int RC_STORES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        int storeId = (getIntent().getExtras() != null) ?getIntent().getExtras().getInt("STOREID", 0)  : 0;

        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        toolbar = findViewById(R.id.toolbar);
        Toolbar subToolbar = findViewById(R.id.subToolbar);
        TextView tvTitle = subToolbar.findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //oculta el teclado

        mAppBarConfiguration = new AppBarConfiguration.Builder().build();
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_store_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (storeId > 0) {
            NavGraph graph = navController.getGraph();
            graph.setStartDestination(R.id.nav_store_review);
            navController.setGraph(graph);

            WebService webService = new WebService(this, RC_STORES);
            webService.callService(this, Constants.WS_DOMINIO + Constants.WS_STORES, "/" + storeId, Request.Method.GET, Constants.JSON_TYPE.OBJECT, null);
        }

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            appBarLayout.setExpanded(true);
            tvTitle.setText(destination.getLabel());

            if (getCurrentFocus() != null)
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            //muestro oculto toolbar
            if (destination.getId() == R.id.nav_view_image || destination.getId() == R.id.nav_congrats) {
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
            case RC_STORES:
                try {
                    Store mStore = new Store(response);
                    setDataStore(mStore);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                }
                break;
        }
    }

    private void setDataStore(Store mStore) {
        AddStoreViewModel mViewModel = new ViewModelProvider(this).get(AddStoreViewModel.class);
        mViewModel.setName(mStore.getName());
        mViewModel.setDesc(mStore.getDescription());
        mViewModel.setProvince(mStore.getAddress());
        mViewModel.setImageLogo(Util.base64ToBitmap(mStore.getStoreLogo()));
        mViewModel.setmImagePortada(Util.base64ToBitmap(mStore.getStoreCoverPhoto()));
    }

    @Override
    public void onResult(int requestCode, JSONArray response) {

    }

    @Override
    public void onError(int requestCode, String message) {

    }
}