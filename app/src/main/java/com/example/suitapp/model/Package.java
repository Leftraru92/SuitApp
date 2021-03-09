package com.example.suitapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Package {
    private Store store;
    private List<Article> articleList;
    private String selectedShiping;
    private int priceShipping;
    private boolean mailing;

    public Package(Store store, List<Article> articles) {
        this.store = store;
        this.articleList = articles;
    }

    public Store getStore() {
        return store;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public String getSelectedShiping() {
        return selectedShiping;
    }

    public void setSelectedShiping(String selectedShiping) {
        this.selectedShiping = selectedShiping;
    }

    public int getPriceShipping() {
        return priceShipping;
    }

    public void setPriceShipping(int priceShipping) {
        this.priceShipping = priceShipping;
    }

    public boolean isMailing() {
        return mailing;
    }

    public void setMailing(boolean mailing) {
        this.mailing = mailing;
    }

    public JSONObject toJson(String email, int addressId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("addressId", addressId);
            jsonObject.put("mailing", isMailing());
            JSONArray carritos = new JSONArray();
            for (Article art: articleList) {
                JSONObject jo = new JSONObject();
                jo.put("articleId", art.getId());
                jo.put("colorId", art.getColorId());
                jo.put("sizeId", art.getSizeId());
                jo.put("quantity", art.getQuantity());
                carritos.put(jo);
            }
            jsonObject.put("carritos", carritos);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
