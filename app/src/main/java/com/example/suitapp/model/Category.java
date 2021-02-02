package com.example.suitapp.model;

public class Category {
    private int id;
    private String name;
    private int visits;

    public Category(int id, String name, int visits) {
        this.id = id;
        this.name = name;
        this.visits = visits;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVisits() {
        return visits;
    }
}
