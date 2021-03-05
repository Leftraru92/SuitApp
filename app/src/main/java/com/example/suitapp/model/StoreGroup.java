package com.example.suitapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreGroup {
    int id;
    String title;
    List<Store> storeList;

    public StoreGroup(JSONObject dataItem) throws JSONException {
        id = dataItem.getInt("groupId");
        title = dataItem.getString("title");
        storeList = new ArrayList<>();

        JSONArray articlesArray = dataItem.getJSONArray("stores");
        for (int i = 0; i < articlesArray.length(); i++) {
            JSONObject storeItem = articlesArray.getJSONObject(i);
            storeList.add(new Store(storeItem));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }
}
