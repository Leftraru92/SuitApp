package com.example.suitapp.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Article {
    private int id;
    private String name;
    private float price;
    private int image;
    private int colors;

    public Article(int id, String name, float price, int image, int colors) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.colors = colors;
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

    public int getColors() {
        return colors;
    }
}
