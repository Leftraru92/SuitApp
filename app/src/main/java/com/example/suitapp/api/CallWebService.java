package com.example.suitapp.api;

import org.json.JSONArray;
import org.json.JSONObject;

public interface CallWebService {
    void onResult(int requestCode, JSONObject response);
    void onResult(int requestCode, JSONArray response);
    void onError(int requestCode, String message);
}
