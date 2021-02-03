package com.example.suitapp.model;

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
        return price;
    }

    public int getImage() {
        return image;
    }

    public int getColors() {
        return colors;
    }
}
