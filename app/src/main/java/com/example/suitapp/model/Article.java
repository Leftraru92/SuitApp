package com.example.suitapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Article {
    private int id;
    private String name;
    private float price;
    private int image;
    private String articleImage;
    private int colors;

    public Article(int id, String name, float price, int image, int colors) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.colors = colors;
    }

    public Article(JSONObject dataItem) throws JSONException {
        id = dataItem.getInt("articleId");
        name = dataItem.getString("articleName");
        price = dataItem.getInt("articlePrice");
        if (dataItem.getString("articleImage") != null && !dataItem.getString("articleImage").equals("null"))
            articleImage = dataItem.getString("articleImage");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        DecimalFormat formatea = new DecimalFormat("###,###.##");
        return price;
    }

    public String getPriceFormated() {
        DecimalFormat formatea = new DecimalFormat("$ ###,###.##", DecimalFormatSymbols.getInstance(Locale.ITALY));
        return formatea.format(price);
    }

    public int getImage() {
        return image;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public int getColors() {
        return colors;
    }
}
