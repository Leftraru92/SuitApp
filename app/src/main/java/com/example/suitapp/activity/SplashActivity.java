package com.example.suitapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.database.QueryDbInsert;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Variant;
import com.example.suitapp.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements CallWebService {
    final int RC_CATEGORIES = 1, RC_GENDERS = 2, RC_COLORS = 3, RC_SIZES = 4;
    private List<Category> categoryList;
    private List<Gender> genderList;
    List<Variant.Color> colorList;
    List<Variant.Size> sizeList;
    boolean gotCat, gotGender, gotColor, gotSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        categoryList = new ArrayList<>();
        genderList = new ArrayList<>();
        colorList = new ArrayList<>();
        sizeList = new ArrayList<>();
        callWs();
    }

    private void callWs() {
        WebService webService = new WebService(this, RC_CATEGORIES);
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_CATEGORIES, null, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

        WebService webServiceG = new WebService(this, RC_GENDERS);
        webServiceG.callService(this, Constants.WS_DOMINIO + Constants.WS_GENDERS, null, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

        WebService webServiceC = new WebService(this, RC_COLORS);
        webServiceC.callService(this, Constants.WS_DOMINIO + Constants.WS_COLORS, null, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

        WebService webServiceS = new WebService(this, RC_SIZES);
        webServiceS.callService(this, Constants.WS_DOMINIO + Constants.WS_SIZES, null, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

    }

    @Override
    public void onResult(int requestCode, JSONObject response) {

    }

    @Override
    public void onResult(int requestCode, JSONArray response) {
        switch (requestCode) {
            case RC_CATEGORIES:
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        categoryList.add(new Category(dataItem));
                    }
                    //Inserto en la base local
                    QueryDbInsert.insertCategories(this, categoryList);
                    gotCat = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    runApp();
                }
                break;
            case RC_GENDERS:
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        genderList.add(new Gender(dataItem));
                    }
                    //Inserto en la base local
                    QueryDbInsert.insertGender(this, genderList);
                    gotGender = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    runApp();
                }
                break;
            case RC_COLORS:
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        colorList.add(new Variant.Color(dataItem));
                    }
                    //Inserto en la base local
                    QueryDbInsert.insertColor(this, colorList);
                    gotColor = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    runApp();
                }
                break;
            case RC_SIZES:
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject dataItem = response.getJSONObject(i);
                        sizeList.add(new Variant.Size(dataItem) {
                        });
                    }
                    //Inserto en la base local
                    QueryDbInsert.insertSize(this, sizeList);
                    gotSize = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    runApp();
                }
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {

    }

    private void runApp(){
        if(gotColor && gotGender && gotCat && gotSize) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}