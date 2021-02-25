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
import com.example.suitapp.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements CallWebService {
    final int RC_CATEGORIES = 1, RC_GENDERS = 2;
    private List<Category> categoryList;
    private List<Gender> genderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        categoryList = new ArrayList<>();
        genderList = new ArrayList<>();
        callWs();
    }

    private void callWs() {
        WebService webService = new WebService(this, RC_CATEGORIES);
        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_CATEGORIES, null, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

        WebService webServiceG = new WebService(this, RC_GENDERS);
        webServiceG.callService(this, Constants.WS_DOMINIO + Constants.WS_GENDERS, null, Request.Method.GET, Constants.JSON_TYPE.ARRAY, null);

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
                } catch (JSONException e) {
                    e.printStackTrace();
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runApp();
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {

    }

    private void runApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}