package com.example.suitapp.model;

public class Genre {
    private int id;
    private String name;
    private int image;
    private int visits;

    public Genre(int id, String name, int image, int visits) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.visits = visits;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getVisits() {
        return visits;
    }
}
